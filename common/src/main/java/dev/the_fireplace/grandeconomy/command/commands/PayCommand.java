package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class PayCommand extends GeCommand {

    private final ArgumentTypeFactory argumentTypeFactory;

    @Inject
    public PayCommand(Economy economy, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory, Requirements requirements, ArgumentTypeFactory argumentTypeFactory) {
        super(economy, translatorFactory, feedbackSenderFactory, requirements);
        this.argumentTypeFactory = argumentTypeFactory;
    }

    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("pay")
            .requires(requirements::player)
            .then(Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::execute)
                )
            )
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.pay.negative");
        }
        UUID senderAccountId = command.getSource().getPlayerOrException().getUUID();
        if (economy.getBalance(senderAccountId, true) < amount) {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, economy.getCurrencyName(2));
        }

        boolean taken = economy.takeFromBalance(senderAccountId, amount, true);
        if (taken) {
            UUID targetAccountId = targetPlayer.getId();
            economy.addToBalance(targetAccountId, amount, true);
            feedbackSender.basic(command, "commands.grandeconomy.pay.paid", economy.formatCurrency(amount), targetPlayer.getName());
            ServerPlayer targetPlayerEntity = targetPlayer.entity();
            if (targetPlayerEntity != null) {
                feedbackSender.basic(targetPlayerEntity, "commands.grandeconomy.pay.recieved", economy.formatCurrency(amount), command.getSource().getDisplayName());
            }

            return Command.SINGLE_SUCCESS;
        } else {
            return feedbackSender.throwFailure(command, CommonTranslationKeys.INSUFFICIENT_CREDIT, economy.getCurrencyName(2));
        }
    }
}
