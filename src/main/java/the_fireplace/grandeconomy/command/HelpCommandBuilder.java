package the_fireplace.grandeconomy.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import the_fireplace.grandeconomy.command.framework.RegisterableCommand;
import the_fireplace.lib.api.chat.TextPaginator;
import the_fireplace.lib.api.chat.Translator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class HelpCommandBuilder implements RegisterableCommand {
    private final TextPaginator textPaginator;
    private final Translator translator;
    private final String modid;
    private final LiteralArgumentBuilder<ServerCommandSource> helpCommandBase;
    private String[] commands = {};

    public HelpCommandBuilder(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase) {
        this.translator = translator;
        this.textPaginator = textPaginator;
        this.modid = modid;
        this.helpCommandBase = helpCommandBase;
    }

    public HelpCommandBuilder addCommands(CommandNode<ServerCommandSource>... commands) {
        String[] commandNames = Arrays.stream(commands).map(CommandNode::getName).toArray(String[]::new);
        this.commands = ArrayUtils.addAll(this.commands, commandNames);

        return this;
    }

    public HelpCommandBuilder addCommands(String... commands) {
        this.commands = ArrayUtils.addAll(this.commands, commands);

        return this;
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(helpCommandBase
            .executes((command) -> runHelpCommand(command, 1))
            .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> runHelpCommand(command, command.getArgument("page", Integer.class)))
            )
        );
    }

    private int runHelpCommand(CommandContext<ServerCommandSource> command, Integer page) {
        textPaginator.sendPaginatedChat(command.getSource(), "/" + helpCommandBase.getLiteral() + " %s", getHelpsList(command), page);
        return Command.SINGLE_SUCCESS;
    }

    private List<Text> getHelpsList(CommandContext<ServerCommandSource> command) {
        List<Text> helps = Lists.newArrayList();
        for (String commandName: commands) {
            MutableText commandHelp = translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + ".usage")
                .append(": ")
                .append(translator.getTextForTarget(command.getSource(), "commands." + modid + "." + commandName + ".description"));
            helps.add(commandHelp);
        }
        helps.sort(Comparator.comparing(Text::getString));
        return helps;
    }
}
