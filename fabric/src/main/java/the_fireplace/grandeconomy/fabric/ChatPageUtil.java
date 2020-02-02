package the_fireplace.grandeconomy.fabric;

import com.google.common.collect.Lists;
import net.minecraft.server.command.CommandSource;
import the_fireplace.grandeconomy.fabric.translation.TranslationUtil;

public class ChatPageUtil {

    public static void showPaginatedChat(CommandSource target, String command, List<ITextComponent> items, int page) {
        if(target.getEntity() != null)
            showPaginatedChat(target.getEntity(), command, items, page);
        else
            showPaginatedChat(target.getServer(), command, items, page);
    }

    public static void showPaginatedChat(ICommandSource target, String command, List<ITextComponent> items, int page) {
        int resultsOnPage = 7;
        int current = page;
        int totalPageCount = items.size() % resultsOnPage > 0 ? (items.size()/resultsOnPage)+1 : items.size()/resultsOnPage;

        ITextComponent counter = TranslationUtil.getTranslation(target, "grandeconomy.chat.page.num", current, totalPageCount);
        ITextComponent top = new StringTextComponent("-----------------").setStyle(TextStyles.GREEN).appendSibling(counter).appendText("-------------------").setStyle(TextStyles.GREEN);

        //Expand page to be the first entry on the page
        page *= resultsOnPage;
        //Subtract result count because the first page starts with entry 0
        page -= resultsOnPage;
        int termLength = resultsOnPage;
        List<ITextComponent> printItems = Lists.newArrayList();

        for (ITextComponent item: items) {
            if (page-- > 0)
                continue;
            if (termLength-- <= 0)
                break;
            printItems.add(item);
        }

        ITextComponent nextButton = current < totalPageCount ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.next").setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(command, current+1)))) : new StringTextComponent("-----");
        ITextComponent prevButton = current > 1 ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.prev").setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(command, current-1)))) : new StringTextComponent("------");
        ITextComponent bottom = new StringTextComponent("---------------").setStyle(TextStyles.GREEN).appendSibling(prevButton).appendText("---").setStyle(TextStyles.GREEN).appendSibling(nextButton).appendText("-------------").setStyle(TextStyles.GREEN);

        target.sendMessage(top);

        for(ITextComponent item: printItems)
            target.sendMessage(item);

        target.sendMessage(bottom);
    }
}
