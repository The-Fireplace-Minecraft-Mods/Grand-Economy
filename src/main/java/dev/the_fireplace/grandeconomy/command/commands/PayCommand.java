package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public final class PayCommand extends GeCommand {
    
    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("pay")
            .requires(requirements::player)
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
            return feedbackSender.throwFailure(command, "commands.grandeconomy.pay.negative");
        }
        if (currencyAPI.getBalance(targetPlayer.getUuid(), true) < amount) {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }

        boolean taken = currencyAPI.takeFromBalance(command.getSource().getPlayer().getUuid(), amount, true);
        if (taken) {
            currencyAPI.addToBalance(targetPlayer.getUuid(), amount, true);
            feedbackSender.basic(command, "commands.grandeconomy.pay.paid", currencyAPI.formatCurrency(amount), targetPlayer.getDisplayName());
            feedbackSender.basic(targetPlayer, "commands.grandeconomy.pay.recieved", currencyAPI.formatCurrency(amount), command.getSource().getName());

            return Command.SINGLE_SUCCESS;
        } else {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }
    }
}
