package the_fireplace.grandeconomy.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.command.framework.RegisterableCommand;
import the_fireplace.lib.api.chat.TextPaginator;
import the_fireplace.lib.api.chat.Translator;

import java.util.Comparator;
import java.util.List;

public final class HelpCommand implements RegisterableCommand {
    //Used for the help command, each command in this list should have a corresponding command usage and description in the lang files
    private static final List<String> commands = Lists.newArrayList("balance", "pay", "wallet", "gehelp");
    private final TextPaginator textPaginator = TextPaginator.getInstance();
    private final Translator translator = GrandEconomy.getTranslator();

    @Override
    public void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(CommandManager.literal("gehelp").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(0))
            .executes((command) -> runHelpCommand(command, 1))
            .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> runHelpCommand(command, command.getArgument("page", Integer.class)))
            )
        );
    }

    private int runHelpCommand(CommandContext<ServerCommandSource> command, Integer page) {
        textPaginator.sendPaginatedChat(command.getSource(), "/gehelp %s", getHelpsList(command), page);
        return Command.SINGLE_SUCCESS;
    }

    private List<Text> getHelpsList(CommandContext<ServerCommandSource> command) {
        List<Text> helps = Lists.newArrayList();
        for (String commandName: commands) {
            MutableText commandHelp = translator.getTextForTarget(command.getSource(), "commands.grandeconomy." + commandName + ".usage")
                .append(": ")
                .append(translator.getTextForTarget(command.getSource(), "commands.grandeconomy." + commandName + ".description"));
            helps.add(commandHelp);
        }
        helps.sort(Comparator.comparing(Text::getString));
        return helps;
    }
}
