package the_fireplace.grandeconomy.fabric;

import com.google.common.collect.Lists;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.fabric.translation.TranslationUtil;

import java.util.List;

public class ChatPageUtil {

    public static void showPaginatedChat(ServerCommandSource target, String command, List<Text> items, int page) {
        if(target.getEntity() != null)
            showPaginatedChat(target.getEntity(), command, items, page);
        else
            showPaginatedChat(target.getMinecraftServer(), command, items, page);
    }

    public static void showPaginatedChat(CommandOutput target, String command, List<Text> items, int page) {
        int resultsOnPage = 7;
        int current = page;
        int totalPageCount = items.size() % resultsOnPage > 0 ? (items.size()/resultsOnPage)+1 : items.size()/resultsOnPage;

        Text counter = TranslationUtil.getTranslation(target, "grandeconomy.chat.page.num", current, totalPageCount);
        Text top = new LiteralText("-----------------").setStyle(TextStyles.GREEN).append(counter).append("-------------------").setStyle(TextStyles.GREEN);

        //Expand page to be the first entry on the page
        page *= resultsOnPage;
        //Subtract result count because the first page starts with entry 0
        page -= resultsOnPage;
        int termLength = resultsOnPage;
        List<Text> printItems = Lists.newArrayList();

        for (Text item: items) {
            if (page-- > 0)
                continue;
            if (termLength-- <= 0)
                break;
            printItems.add(item);
        }

        Text nextButton = current < totalPageCount ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.next").setStyle(new Style().setClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(command, current+1)))) : new LiteralText("-----");
        Text prevButton = current > 1 ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.prev").setStyle(new Style().setClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(command, current-1)))) : new LiteralText("------");
        Text bottom = new LiteralText("---------------").setStyle(TextStyles.GREEN).append(prevButton).append("---").setStyle(TextStyles.GREEN).append(nextButton).append("-------------").setStyle(TextStyles.GREEN);

        target.sendMessage(top);

        for(Text item: printItems)
            target.sendMessage(item);

        target.sendMessage(bottom);
    }
}
