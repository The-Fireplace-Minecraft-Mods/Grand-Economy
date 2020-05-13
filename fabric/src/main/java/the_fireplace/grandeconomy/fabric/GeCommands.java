package the_fireplace.grandeconomy.fabric;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.fabric.translation.TranslationUtil;

import java.util.Comparator;
import java.util.List;

public class GeCommands {
    //Used for the help command, each command in this list should have a corresponding command usage and description in the lang files
    private static final List<String> commands = Lists.newArrayList("balance", "convert", "pay", "wallet", "gehelp");

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(CommandManager.literal("balance").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity).executes((command) -> {
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().getPlayer().getUuid(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(command.getSource().getPlayer().getUuid(), true)), false);
            return Command.SINGLE_SUCCESS;
        }));

        commandDispatcher.register(CommandManager.literal("pay").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                        .executes((command) -> {
                            ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
                            double amount = command.getArgument("amount", Integer.class);
                            if (amount < 0)
                                throw new CommandException(TranslationUtil.getTranslation(targetPlayer.getUuid(), "commands.grandeconomy.pay.negative").setStyle(TextStyles.RED));
                            if (GrandEconomyApi.getBalance(targetPlayer.getUuid(), true) < amount)
                                throw new CommandException(TranslationUtil.getTranslation(command.getSource().getPlayer().getUuid(), "commands.grandeconomy.common.insufficient_credit", GrandEconomyApi.getCurrencyName(2)).setStyle(TextStyles.RED));
                            GrandEconomyApi.takeFromBalance(command.getSource().getPlayer().getUuid(), amount, true);
                            GrandEconomyApi.addToBalance(targetPlayer.getUuid(), amount, true);
                            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().getPlayer().getUuid(), "commands.grandeconomy.pay.paid", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                            targetPlayer.sendMessage(TranslationUtil.getTranslation(targetPlayer.getUuid(), "commands.grandeconomy.pay.recieved", GrandEconomyApi.formatCurrency(amount), command.getSource().getName()));
                            return Command.SINGLE_SUCCESS;
                        }))));


        LiteralArgumentBuilder<ServerCommandSource> walletCommand = CommandManager.literal("wallet").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermissionLevel(2));

        walletCommand.then(CommandManager.literal("balance").executes((command) -> {
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().getPlayer().getUuid(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(command.getSource().getPlayer().getUuid(), true)), false);
            return Command.SINGLE_SUCCESS;
        }).then(CommandManager.argument("player", EntityArgumentType.player()).executes((command) -> {
            ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.balance", targetPlayer.getName(), GrandEconomyApi.getBalanceFormatted(targetPlayer.getUuid(), true)), false);
            return Command.SINGLE_SUCCESS;
        })));
        walletCommand.then(CommandManager.literal("set")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
                    double amount = command.getArgument("amount", Integer.class);
                    if (amount < 0)
                        throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                    GrandEconomyApi.setBalance(targetPlayer.getUuid(), amount, true);
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.set", targetPlayer.getName(), GrandEconomyApi.formatCurrency(amount)), false);
                    return Command.SINGLE_SUCCESS;
        }))));
        ArgumentBuilder<ServerCommandSource, ?> giveArgs =
                CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                        .executes((command) -> {
                            PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "player");
                            double amount = command.getArgument("amount", Integer.class);
                            if(GrandEconomyApi.getBalance(targetPlayer.getUuid(), true) + amount < 0)
                                throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                            GrandEconomyApi.addToBalance(targetPlayer.getUuid(), amount, true);
                            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.given", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                            return Command.SINGLE_SUCCESS;
                        }));
        walletCommand.then(CommandManager.literal("give")
                .then(giveArgs));
        walletCommand.then(CommandManager.literal("add")
                .then(giveArgs));
        walletCommand.then(CommandManager.literal("take")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity targetPlayer = EntityArgumentType.getPlayer(command, "target");
                    double amount = command.getArgument("amount", Integer.class);
                    if(GrandEconomyApi.getBalance(targetPlayer.getUuid(), true) - amount < 0)
                        throw new CommandException(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.negative", targetPlayer.getName()).setStyle(TextStyles.RED));
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.taken", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                    return Command.SINGLE_SUCCESS;
                }))));

        commandDispatcher.register(walletCommand);

        commandDispatcher.register(CommandManager.literal("gehelp").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(0))
                .executes((command) -> {
                    ChatPageUtil.showPaginatedChat(command.getSource(), "/gehelp %s", getHelpsList(command), 1);
                    return Command.SINGLE_SUCCESS;
                })
                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> {
                    ChatPageUtil.showPaginatedChat(command.getSource(), "/gehelp %s", getHelpsList(command), command.getArgument("page", Integer.class));
                    return Command.SINGLE_SUCCESS;
                })));
    }

    private static List<Text> getHelpsList(CommandContext<ServerCommandSource> command) {
        List<Text> helps = Lists.newArrayList();
        for(String commandName: commands)
            helps.add(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.gehelp.format",
                    TranslationUtil.getStringTranslation("commands.grandeconomy."+commandName+".usage"),
                    TranslationUtil.getStringTranslation("commands.grandeconomy."+commandName+".description")));
        helps.sort(Comparator.comparing(Text::getString));
        return helps;
    }
}
