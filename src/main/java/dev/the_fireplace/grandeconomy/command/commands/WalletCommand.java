package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class WalletCommand extends GeCommand {

    private final ArgumentTypeFactory argumentTypeFactory;

    @Inject
    public WalletCommand(CurrencyAPI currencyAPI, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory, Requirements requirements, ArgumentTypeFactory argumentTypeFactory) {
        super(currencyAPI, translatorFactory, feedbackSenderFactory, requirements);
        this.argumentTypeFactory = argumentTypeFactory;
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> walletCommand = CommandManager.literal("wallet")
            .requires(requirements::manageGameSettings);

        walletCommand.then(CommandManager.literal("balance")
            .then(CommandManager.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .executes(this::runBalanceCommand)
            )
        );
        walletCommand.then(CommandManager.literal("set")
            .then(CommandManager.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runSetCommand)
                )
            )
        );

        addAliased(walletCommand, new String[]{"give", "add"},
            CommandManager.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runGiveCommand)
                )
        );

        walletCommand.then(CommandManager.literal("take")
            .then(CommandManager.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runTakeCommand)
                )
            )
        );

        return commandDispatcher.register(walletCommand);
    }

    private void addAliased(ArgumentBuilder<ServerCommandSource, ?> baseCommand, String[] aliases, ArgumentBuilder<ServerCommandSource, ?> remainingArgs) {
        for (String alias: aliases) {
            baseCommand.then(CommandManager.literal(alias)
                .then(remainingArgs)
            );
        }
    }

    private int runSetCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        currencyAPI.setBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.set", targetPlayer.getName(), currencyAPI.formatCurrency(amount));
        return Command.SINGLE_SUCCESS;
    }

    private int runBalanceCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        feedbackSender.basic(command, "commands.grandeconomy.wallet.balance", targetPlayer.getName(), currencyAPI.getFormattedBalance(targetPlayer.getId(), true));
        return Command.SINGLE_SUCCESS;
    }

    private int runGiveCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (currencyAPI.getBalance(targetPlayer.getId(), true) + amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        currencyAPI.addToBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.given", currencyAPI.formatCurrency(amount), targetPlayer.getName());
        return Command.SINGLE_SUCCESS;
    }

    private int runTakeCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (currencyAPI.getBalance(targetPlayer.getId(), true) - amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        currencyAPI.takeFromBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.taken", currencyAPI.formatCurrency(amount), targetPlayer.getName());
        return Command.SINGLE_SUCCESS;
    }
}
