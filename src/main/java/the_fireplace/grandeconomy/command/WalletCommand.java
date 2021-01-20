package the_fireplace.grandeconomy.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.command.framework.CommandManager;
import the_fireplace.grandeconomy.command.framework.RegisterableCommand;
import the_fireplace.grandeconomy.command.framework.Requirements;
import the_fireplace.grandeconomy.command.framework.SendFeedback;

public final class WalletCommand implements RegisterableCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> walletCommand = CommandManager.literal("wallet")
            .requires(Requirements::manageGameSettings);

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
        walletCommand.then(CommandManager.aliased("give", "add")
            .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runGiveCommand)
                )
            )
        );
        walletCommand.then(CommandManager.literal("take")
            .then(CommandManager.argument("target", EntityArgumentType.player())
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg(0))
                    .executes(this::runTakeCommand)
                )
            )
        );

        commandDispatcher.register(walletCommand);
    }

    private int runSetCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (amount < 0) {
            return SendFeedback.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        GrandEconomyApi.setBalance(targetPlayer.getUuid(), amount, true);
        command.getSource().sendFeedback(GrandEconomy.getTranslator().getTextForTarget(command.getSource(), "commands.grandeconomy.wallet.set", targetPlayer.getDisplayName(), GrandEconomyApi.formatCurrency(amount)), false);
        return Command.SINGLE_SUCCESS;
    }

    private int runBalanceCommand(CommandContext<ServerCommandSource> command, ServerPlayerEntity targetPlayer) {
        SendFeedback.basic(command, "commands.grandeconomy.wallet.balance", targetPlayer.getDisplayName(), GrandEconomyApi.getBalanceFormatted(targetPlayer.getUuid(), true));
        return Command.SINGLE_SUCCESS;
    }

    private int runGiveCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
        double amount = command.getArgument("amount", Double.class);
        if (GrandEconomyApi.getBalance(targetPlayer.getUuid(), true) + amount < 0) {
            return SendFeedback.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        GrandEconomyApi.addToBalance(targetPlayer.getUuid(), amount, true);
        SendFeedback.basic(command, "commands.grandeconomy.wallet.given", GrandEconomyApi.formatCurrency(amount), targetPlayer.getDisplayName());
        return Command.SINGLE_SUCCESS;
    }

    private int runTakeCommand(CommandContext<ServerCommandSource> command) throws CommandSyntaxException {
        PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "target");
        double amount = command.getArgument("amount", Double.class);
        if (GrandEconomyApi.getBalance(targetPlayer.getUuid(), true) - amount < 0) {
            return SendFeedback.throwFailure(command, "commands.grandeconomy.wallet.negative", targetPlayer.getDisplayName());
        }
        SendFeedback.basic(command, "commands.grandeconomy.wallet.taken", GrandEconomyApi.formatCurrency(amount), targetPlayer.getDisplayName());
        return Command.SINGLE_SUCCESS;
    }
}
