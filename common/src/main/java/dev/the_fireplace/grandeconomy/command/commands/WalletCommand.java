package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class WalletCommand extends GeCommand {

    private final ArgumentTypeFactory argumentTypeFactory;

    @Inject
    public WalletCommand(Economy economy, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory, Requirements requirements, ArgumentTypeFactory argumentTypeFactory) {
        super(economy, translatorFactory, feedbackSenderFactory, requirements);
        this.argumentTypeFactory = argumentTypeFactory;
    }

    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> walletCommand = Commands.literal("wallet")
            .requires(requirements::manageGameSettings);

        walletCommand.then(Commands.literal("balance")
            .then(Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .executes(this::runBalanceCommand)
            )
        );
        walletCommand.then(Commands.literal("set")
            .then(Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runSetCommand)
                )
            )
        );

        addAliased(walletCommand, new String[]{"give", "add"},
            Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runGiveCommand)
                )
        );

        walletCommand.then(Commands.literal("take")
            .then(Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .then(Commands.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runTakeCommand)
                )
            )
        );

        return commandDispatcher.register(walletCommand);
    }

    private void addAliased(ArgumentBuilder<CommandSourceStack, ?> baseCommand, String[] aliases, ArgumentBuilder<CommandSourceStack, ?> remainingArgs) {
        for (String alias : aliases) {
            baseCommand.then(Commands.literal(alias)
                .then(remainingArgs)
            );
        }
    }

    private int runSetCommand(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        economy.setBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.set", targetPlayer.getName(), economy.formatCurrency(amount));
        return Command.SINGLE_SUCCESS;
    }

    private int runBalanceCommand(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        feedbackSender.basic(command, "commands.grandeconomy.wallet.balance", targetPlayer.getName(), economy.getFormattedBalance(targetPlayer.getId(), true));
        return Command.SINGLE_SUCCESS;
    }

    private int runGiveCommand(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (economy.getBalance(targetPlayer.getId(), true) + amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        economy.addToBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.given", economy.formatCurrency(amount), targetPlayer.getName());
        return Command.SINGLE_SUCCESS;
    }

    private int runTakeCommand(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (economy.getBalance(targetPlayer.getId(), true) - amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getName());
        }
        economy.takeFromBalance(targetPlayer.getId(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.taken", economy.formatCurrency(amount), targetPlayer.getName());
        return Command.SINGLE_SUCCESS;
    }
}
