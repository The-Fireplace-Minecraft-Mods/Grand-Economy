package the_fireplace.grandeconomy.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.grandeconomy.api.CurrencyAPI;
import the_fireplace.grandeconomy.command.framework.RegisterableCommand;
import the_fireplace.grandeconomy.command.framework.Requirements;
import the_fireplace.grandeconomy.command.framework.SendFeedback;

public final class BalanceCommand implements RegisterableCommand {

    private final CurrencyAPI currencyAPI;

    BalanceCommand() {
        this.currencyAPI = CurrencyAPI.getInstance();
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("balance")
            .requires(Requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        SendFeedback.basic(command, "commands.grandeconomy.common.balance", currencyAPI.getBalanceFormatted(command.getSource().getPlayer().getUuid(), true));
        return Command.SINGLE_SUCCESS;
    }
}
