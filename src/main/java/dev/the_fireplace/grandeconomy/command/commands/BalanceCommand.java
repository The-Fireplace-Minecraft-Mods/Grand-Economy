package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class BalanceCommand extends GeCommand {

    @Inject
    public BalanceCommand(
        CurrencyAPI currencyAPI,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory,
        Requirements requirements
    ) {
        super(currencyAPI, translatorFactory, feedbackSenderFactory, requirements);
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("balance")
            .requires(requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        feedbackSender.basic(command, CommonTranslationKeys.BALANCE, currencyAPI.getFormattedBalance(command.getSource().getPlayer().getUuid(), true));
        return Command.SINGLE_SUCCESS;
    }
}
