package the_fireplace.grandeconomy.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.api.CurrencyAPI;
import the_fireplace.grandeconomy.command.framework.CommonTranslationKeys;
import the_fireplace.grandeconomy.command.framework.RegisterableCommand;
import the_fireplace.grandeconomy.command.framework.Requirements;
import the_fireplace.grandeconomy.command.framework.SendFeedback;

public final class PayCommand implements RegisterableCommand {

    private final CurrencyAPI currencyAPI;

    PayCommand() {
        this.currencyAPI = CurrencyAPI.getInstance();
    }
    
    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("pay")
            .requires(Requirements::player)
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::execute)
                )
            )
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) throws CommandException, CommandSyntaxException {
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return SendFeedback.throwFailure(command, "commands.grandeconomy.pay.negative");
        }
        if (currencyAPI.getBalance(targetPlayer.getUuid(), true) < amount) {
            return SendFeedback.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }

        boolean taken = currencyAPI.takeFromBalance(command.getSource().getPlayer().getUuid(), amount, true);
        if (taken) {
            currencyAPI.addToBalance(targetPlayer.getUuid(), amount, true);
            SendFeedback.basic(command, "commands.grandeconomy.pay.paid", currencyAPI.formatCurrency(amount), targetPlayer.getDisplayName());
            SendFeedback.basic(targetPlayer, "commands.grandeconomy.pay.recieved", currencyAPI.formatCurrency(amount), command.getSource().getName());

            return Command.SINGLE_SUCCESS;
        } else {
            return SendFeedback.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }
    }
}
