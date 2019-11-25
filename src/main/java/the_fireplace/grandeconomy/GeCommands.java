package the_fireplace.grandeconomy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.economy.Account;
import the_fireplace.grandeconomy.translation.TranslationUtil;

public class GeCommands {

    @SuppressWarnings("Duplicates")
    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("balance").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity).executes((command) -> {
            Account account = Account.get(command.getSource().asPlayer());
            account.update();
            long balance = account.getBalance();
            command.getSource().sendFeedback(new TranslationTextComponent("Balance: %s", balance), false);
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
                            //Account senderAccount = Account.get(command.getSource().asPlayer());
                            //if (senderAccount.getBalance() < amount)
                                //;//TODO Replace insufficientcreditexception with a normal error message like everything else uses
                            //senderAccount.addBalance(-amount, true);
                            //account.addBalance(amount, true);
                            return 1;
                        })));


        LiteralArgumentBuilder<CommandSource> walletCommand = Commands.literal("wallet").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermissionLevel(2));

        walletCommand.then(Commands.literal("balance").executes((command) -> {
            Account account = Account.get(command.getSource().asPlayer());
            account.update();
            long balance = account.getBalance();
            command.getSource().sendFeedback(new TranslationTextComponent("Balance: %s", balance), false);
            return 1;
        }).then(Commands.argument("target", EntityArgument.player()).executes((command) -> {
            PlayerEntity target = EntityArgument.getPlayer(command, "target");
            Account pAccount = Account.get(target);
            pAccount.update();
            command.getSource().sendFeedback(new TranslationTextComponent("%s balance: %s", target.getName(), pAccount.getBalance()), false);
            return 1;
        })));
        walletCommand.then(Commands.literal("set")
                .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
            PlayerEntity target = EntityArgument.getPlayer(command, "target");
            Account pAccount = Account.get(target);
            pAccount.setBalance(IntegerArgumentType.getInteger(command, "amount"), false);
            command.getSource().sendFeedback(new TranslationTextComponent("%s balance set to %s", target.getName(), pAccount.getBalance()), false);
            return 1;
        }))));
        walletCommand.then(Commands.literal("give")
                .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity target = EntityArgument.getPlayer(command, "target");
                    Account pAccount = Account.get(target);
                    pAccount.addBalance(IntegerArgumentType.getInteger(command, "amount"), false);
                    command.getSource().sendFeedback(new TranslationTextComponent("%s balance is now %s", target.getName(), pAccount.getBalance()), false);
                    return 1;
                }))));
        walletCommand.then(Commands.literal("add")
                .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity target = EntityArgument.getPlayer(command, "target");
                    Account pAccount = Account.get(target);
                    pAccount.addBalance(IntegerArgumentType.getInteger(command, "amount"), false);
                    command.getSource().sendFeedback(new TranslationTextComponent("%s balance is now %s", target.getName(), pAccount.getBalance()), false);
                    return 1;
                }))));
        walletCommand.then(Commands.literal("take")
                .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    PlayerEntity target = EntityArgument.getPlayer(command, "target");
                    Account pAccount = Account.get(target);
                    pAccount.addBalance(-IntegerArgumentType.getInteger(command, "amount"), false);
                    command.getSource().sendFeedback(new TranslationTextComponent("%s balance is now %s", target.getName(), pAccount.getBalance()), false);
                    return 1;
                }))));

        commandDispatcher.register(walletCommand);
    }
}
