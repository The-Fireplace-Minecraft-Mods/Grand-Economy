package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class PayCommand extends GeCommand {

    private final ArgumentTypeFactory argumentTypeFactory;

    @Inject
    public PayCommand(CurrencyAPI currencyAPI, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory, Requirements requirements, ArgumentTypeFactory argumentTypeFactory) {
        super(currencyAPI, translatorFactory, feedbackSenderFactory, requirements);
        this.argumentTypeFactory = argumentTypeFactory;
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("pay")
            .requires(requirements::player)
            .then(CommandManager.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::execute)
                )
            )
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) throws CommandException, CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.pay.negative");
        }
        UUID senderAccountId = command.getSource().getPlayer().getUuid();
        if (currencyAPI.getBalance(senderAccountId, true) < amount) {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }

        boolean taken = currencyAPI.takeFromBalance(senderAccountId, amount, true);
        if (taken) {
            UUID targetAccountId = targetPlayer.getId();
            currencyAPI.addToBalance(targetAccountId, amount, true);
            feedbackSender.basic(command, "commands.grandeconomy.pay.paid", currencyAPI.formatCurrency(amount), targetPlayer.getName());
            ServerPlayerEntity targetPlayerEntity = targetPlayer.entity();
            if (targetPlayerEntity != null) {
                feedbackSender.basic(targetPlayerEntity, "commands.grandeconomy.pay.recieved", currencyAPI.formatCurrency(amount), command.getSource().getName());
            }

            return Command.SINGLE_SUCCESS;
        } else {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, currencyAPI.getCurrencyName(2));
        }
    }
}
