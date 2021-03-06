package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public final class WalletCommand extends GeCommand {
    
    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> walletCommand = CommandManager.literal("wallet")
            .requires(requirements::manageGameSettings);

        walletCommand.then(CommandManager.literal("balance")
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .executes((command) -> {
                    ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
                    return runBalanceCommand(command, targetPlayer);
                })
            )
        );
        walletCommand.then(CommandManager.literal("set")
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runSetCommand)
                )
            )
        );

        addAliased(walletCommand, new String[]{"give", "add"},
            CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runGiveCommand)
                )
        );

        walletCommand.then(CommandManager.literal("take")
            .then(CommandManager.argument("target", EntityArgumentType.player())
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
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        currencyAPI.setBalance(targetPlayer.getUuid(), amount, true);
        command.getSource().sendFeedback(GrandEconomy.getTranslator().getTextForTarget(command.getSource(), "commands.grandeconomy.wallet.set", targetPlayer.getDisplayName(), currencyAPI.formatCurrency(amount)), false);
        return Command.SINGLE_SUCCESS;
    }

    private int runBalanceCommand(CommandContext<ServerCommandSource> command, ServerPlayerEntity targetPlayer) {
        feedbackSender.basic(command, "commands.grandeconomy.wallet.balance", targetPlayer.getDisplayName(), currencyAPI.getFormattedBalance(targetPlayer.getUuid(), true));
        return Command.SINGLE_SUCCESS;
    }

    private int runGiveCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (currencyAPI.getBalance(targetPlayer.getUuid(), true) + amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        currencyAPI.addToBalance(targetPlayer.getUuid(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.given", currencyAPI.formatCurrency(amount), targetPlayer.getDisplayName());
        return Command.SINGLE_SUCCESS;
    }

    private int runTakeCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "target");
        double amount = command.getArgument("amount", Double.class);
        if (currencyAPI.getBalance(targetPlayer.getUuid(), true) - amount < 0) {
            return feedbackSender.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        currencyAPI.takeFromBalance(targetPlayer.getUuid(), amount, true);
        feedbackSender.basic(command, "commands.grandeconomy.wallet.taken", currencyAPI.formatCurrency(amount), targetPlayer.getDisplayName());
        return Command.SINGLE_SUCCESS;
    }
}
