package the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.grandeconomy.command.GeCommand;

public final class BalanceCommand extends GeCommand {

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("balance")
            .requires(requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        feedbackSender.basic(command, "commands.grandeconomy.common.balance", currencyAPI.getBalanceFormatted(command.getSource().getPlayer().getUuid(), true));
        return Command.SINGLE_SUCCESS;
    }
}
