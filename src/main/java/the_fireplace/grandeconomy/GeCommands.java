package the_fireplace.grandeconomy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

public class GeCommands {

    @SuppressWarnings("Duplicates")
    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("balance").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity).executes((command) -> {
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalance(command.getSource().asPlayer().getUniqueID(), true)), false);
            return 1;
        }));

        commandDispatcher.register(Commands.literal("pay").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
                        .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0)))
                        .executes((command) -> {
                            ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(command, "player");
                            GrandEconomyApi.ensureAccountExists(targetPlayer.getUniqueID(), true);
                            long amount = command.getArgument("amount", Integer.class);
                            if (amount < 0)
                                throw new CommandException(TranslationUtil.getTranslation(targetPlayer.getUniqueID(), "commands.grandeconomy.pay.negative").setStyle(TextStyles.RED));
                            if (GrandEconomyApi.getBalance(targetPlayer.getUniqueID(), true) < amount)
                                throw new CommandException(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.common.insufficient_credit", GrandEconomyApi.getCurrencyName(2)).setStyle(TextStyles.RED));
                            GrandEconomyApi.takeFromBalance(command.getSource().asPlayer().getUniqueID(), amount, true);
                            GrandEconomyApi.addToBalance(targetPlayer.getUniqueID(), amount, true);
                            return 1;
                        })));


        LiteralArgumentBuilder<CommandSource> walletCommand = Commands.literal("wallet").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermissionLevel(2));

        walletCommand.then(Commands.literal("balance").executes((command) -> {
            GrandEconomyApi.ensureAccountExists(command.getSource().asPlayer().getUniqueID(), true);
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalance(command.getSource().asPlayer().getUniqueID(), true)), false);
            return 1;
        }).then(Commands.argument("player", EntityArgument.player()).executes((command) -> {
            ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(command, "player");
            GrandEconomyApi.ensureAccountExists(targetPlayer.getUniqueID(), true);
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.balance", targetPlayer.getName(), GrandEconomyApi.getBalance(targetPlayer.getUniqueID(), true)), false);
            return 1;
        })));
        walletCommand.then(Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity targetPlayer = EntityArgument.getPlayer(command, "player");
                    GrandEconomyApi.ensureAccountExists(targetPlayer.getUniqueID(), true);
                    long amount = command.getArgument("amount", Integer.class);
                    if (amount < 0)
                        throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                    GrandEconomyApi.setBalance(targetPlayer.getUniqueID(), amount, true);
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.set", targetPlayer.getName(), amount), false);
            return 1;
        }))));
        ArgumentBuilder<CommandSource, ?> giveArgs =
                Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes((command) -> {
                            PlayerEntity targetPlayer = EntityArgument.getPlayer(command, "player");
                            long amount = command.getArgument("amount", Integer.class);
                            if(GrandEconomyApi.getBalance(targetPlayer.getUniqueID(), true) + amount < 0)
                                throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                            GrandEconomyApi.addToBalance(targetPlayer.getUniqueID(), amount, true);
                            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.given", amount, targetPlayer.getName()), false);
                            return 1;
                        }));
        walletCommand.then(Commands.literal("give")
                .then(giveArgs));
        walletCommand.then(Commands.literal("add")
                .then(giveArgs));
        walletCommand.then(Commands.literal("take")
                .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity targetPlayer = EntityArgument.getPlayer(command, "target");
                    long amount = command.getArgument("amount", Integer.class);
                    if(GrandEconomyApi.getBalance(targetPlayer.getUniqueID(), true) - amount < 0)
                        throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.taken", amount, targetPlayer.getName()), false);
                    return 1;
                }))));

        commandDispatcher.register(walletCommand);
    }
}
