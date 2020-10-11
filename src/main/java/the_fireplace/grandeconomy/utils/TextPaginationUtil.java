package the_fireplace.grandeconomy.utils;

import com.google.common.collect.Lists;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.TextStyles;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import java.util.Collection;
import java.util.List;

public class TextPaginationUtil {
    public static final int RESULTS_PER_PAGE = 7;

    public static void sendPaginatedChat(ServerCommandSource target, String switchPageCommand, List<Text> allItems, int page) {
        CommandOutput output = target.getEntity() != null ? target.getEntity() : target.getMinecraftServer();
        for(Text text: getPaginatedContent(output, allItems, page, RESULTS_PER_PAGE, switchPageCommand))
            target.sendFeedback(text, false);
    }

    protected static int getTotalPageCount(Collection<?> allItems, int resultsPerPage) {
        int pageCount = allItems.size() / resultsPerPage;
        if(allItems.size() % resultsPerPage > 0)
            pageCount++;
        return pageCount;
    }

    @SuppressWarnings("SameParameterValue")
    protected static List<Text> getPaginatedContent(CommandOutput target, List<Text> allContent, int page, int resultsPerPage, String switchPageCommand) {
        int totalPageCount = getTotalPageCount(allContent, resultsPerPage);

        Text header = getPaginationHeader(target, page, totalPageCount);
        List<Text> content = getPageContents(allContent, page, resultsPerPage);
        Text footer = getPaginationFooter(target, switchPageCommand, page, totalPageCount);

        List<Text> outputTexts = Lists.newArrayList();
        outputTexts.add(header);
        outputTexts.addAll(content);
        outputTexts.add(footer);

        return outputTexts;
    }

    protected static Text getPaginationHeader(CommandOutput target, int currentPage, int totalPageCount) {
        Text counter = TranslationUtil.getTranslation(target, "grandeconomy.chat.page.num", currentPage, totalPageCount);
        return new LiteralText("-----------------").setStyle(TextStyles.GREEN).append(counter).append("-------------------").setStyle(TextStyles.GREEN);
    }

    protected static List<Text> getPageContents(List<Text> allContents, int page, int resultsPerPage) {
        return Lists.partition(allContents, resultsPerPage).get(page-1);
    }

    protected static Text getPaginationFooter(CommandOutput target, String switchPageCommand, int currentPage, int totalPageCount) {
        Text nextButton = currentPage < totalPageCount ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.next").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage +1)))) : new LiteralText("-----");
        Text prevButton = currentPage > 1 ? TranslationUtil.getTranslation(target, "grandeconomy.chat.page.prev").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage -1)))) : new LiteralText("------");
        return new LiteralText("---------------").setStyle(TextStyles.GREEN).append(prevButton).append("---").setStyle(TextStyles.GREEN).append(nextButton).append("-------------").setStyle(TextStyles.GREEN);
    }
}
