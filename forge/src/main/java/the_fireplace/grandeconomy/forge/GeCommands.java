package the_fireplace.grandeconomy.forge;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.forge.earnings.ConversionItems;
import the_fireplace.grandeconomy.forge.translation.TranslationUtil;

import java.util.Comparator;
import java.util.List;

public class GeCommands {
    //Used for the help command, each command in this list should have a corresponding command usage and description in the lang files
    private static final List<String> commands = Lists.newArrayList("balance", "convert", "pay", "wallet", "gehelp");

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("balance").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity).executes((command) -> {
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(command.getSource().asPlayer().getUniqueID(), true)), false);
            return Command.SINGLE_SUCCESS;
        }));

        commandDispatcher.register(Commands.literal("pay").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
                        .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
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
                            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.pay.paid", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                            targetPlayer.sendMessage(TranslationUtil.getTranslation(targetPlayer.getUniqueID(), "commands.grandeconomy.pay.recieved", GrandEconomyApi.formatCurrency(amount), command.getSource().getName()));
                            return Command.SINGLE_SUCCESS;
                        }))));


        LiteralArgumentBuilder<CommandSource> walletCommand = Commands.literal("wallet").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermissionLevel(2));

        walletCommand.then(Commands.literal("balance").executes((command) -> {
            GrandEconomyApi.ensureAccountExists(command.getSource().asPlayer().getUniqueID(), true);
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(command.getSource().asPlayer().getUniqueID(), true)), false);
            return Command.SINGLE_SUCCESS;
        }).then(Commands.argument("player", EntityArgument.player()).executes((command) -> {
            ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(command, "player");
            GrandEconomyApi.ensureAccountExists(targetPlayer.getUniqueID(), true);
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.balance", targetPlayer.getName(), GrandEconomyApi.getBalanceFormatted(targetPlayer.getUniqueID(), true)), false);
            return Command.SINGLE_SUCCESS;
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
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.set", targetPlayer.getName(), GrandEconomyApi.formatCurrency(amount)), false);
                    return Command.SINGLE_SUCCESS;
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
                            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.given", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                            return Command.SINGLE_SUCCESS;
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
                    command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.wallet.taken", GrandEconomyApi.formatCurrency(amount), targetPlayer.getName()), false);
                    return Command.SINGLE_SUCCESS;
                }))));

        commandDispatcher.register(walletCommand);

        commandDispatcher.register(Commands.literal("convert").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
                        .executes((command) -> {
                            ResourceLocation heldResource = command.getSource().asPlayer().getHeldItemMainhand().getItem().getRegistryName();
                            if(heldResource != null && ConversionItems.hasValue(heldResource)) {
                                int value = ConversionItems.getValue(heldResource);
                                int count = command.getSource().asPlayer().getHeldItemMainhand().getCount();
                                GrandEconomyApi.addToBalance(command.getSource().asPlayer().getUniqueID(), value * count, true);
                                command.getSource().asPlayer().setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                                command.getSource().asPlayer().sendMessage(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.convert.success", count, heldResource.toString(), value, value * count, GrandEconomyApi.getBalanceFormatted(command.getSource().asPlayer().getUniqueID(), true)));
                            } else
                                command.getSource().asPlayer().sendMessage(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.grandeconomy.convert.failure"));
                            return Command.SINGLE_SUCCESS;
                        }));

        commandDispatcher.register(Commands.literal("gehelp").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(0))
                .executes((command) -> {
                    ChatPageUtil.showPaginatedChat(command.getSource(), "/gehelp %s", getHelpsList(command), 1);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("page", IntegerArgumentType.integer(1))
                .executes((command) -> {
                    ChatPageUtil.showPaginatedChat(command.getSource(), "/gehelp %s", getHelpsList(command), command.getArgument("page", Integer.class));
                    return Command.SINGLE_SUCCESS;
                })));
    }

    private static List<ITextComponent> getHelpsList(CommandContext<CommandSource> command) {
        List<ITextComponent> helps = Lists.newArrayList();
        for(String commandName: commands)
            helps.add(TranslationUtil.getTranslation(command.getSource(), "commands.grandeconomy.gehelp.format",
                    TranslationUtil.getStringTranslation(command.getSource(), "commands.grandeconomy."+commandName+".usage"),
                    TranslationUtil.getStringTranslation(command.getSource(), "commands.grandeconomy."+commandName+".description")));
        helps.sort(Comparator.comparing(ITextComponent::getUnformattedComponentText));
        return helps;
    }
}
