package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class BalanceCommand extends GeCommand {

    @Inject
    public BalanceCommand(
        Economy economy,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory,
        Requirements requirements
    ) {
        super(economy, translatorFactory, feedbackSenderFactory, requirements);
    }

    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("balance")
            .requires(requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        feedbackSender.basic(command, CommonTranslationKeys.BALANCE, economy.getFormattedBalance(command.getSource().getPlayerOrException().getUUID(), true));
        return Command.SINGLE_SUCCESS;
    }
}
