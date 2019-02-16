package the_fireplace.grandeconomy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.grandeconomy.api.InsufficientCreditException;
import the_fireplace.grandeconomy.economy.Account;

public class GeCommands {

    @SuppressWarnings("Duplicates")
    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("balance").requires((iCommandSender) -> iCommandSender.getEntity() instanceof EntityPlayer).executes((command) -> {
            Account account = Account.get(command.getSource().asPlayer());
            account.update();
            long balance = account.getBalance();
            command.getSource().sendFeedback(new TextComponentTranslation("Balance: %s", balance), false);
            return 1;
        }));

        commandDispatcher.register(Commands.literal("pay").requires((iCommandSender) -> iCommandSender.getEntity() instanceof EntityPlayer)
                        .then(Commands.argument("player", EntityArgument.singlePlayer())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0)))
                        .executes((command) -> {
                            EntityPlayerMP entityplayer = EntityArgument.getOnePlayer(command, "player");
                            Account account = Account.get(entityplayer);
                            account.update();
                            long amount = command.getArgument("amount", Integer.class);
                            if (amount < 0)
                                throw new CommandException(new TextComponentString("You cannot pay someone a negative amount.").setStyle(TextStyles.RED));
                            Account senderAccount = Account.get(command.getSource().asPlayer());
                            if (senderAccount.getBalance() < amount)
                                throw new InsufficientCreditException();
                            senderAccount.addBalance(-amount, true);
                            account.addBalance(amount, true);
                            return 1;
                        })));


        LiteralArgumentBuilder<CommandSource> walletCommand = Commands.literal("wallet").requires((iCommandSender) -> iCommandSender.getEntity() instanceof EntityPlayer && iCommandSender.hasPermissionLevel(2));

        walletCommand.then(Commands.literal("balance").executes((command) -> {
            Account account = Account.get(command.getSource().asPlayer());
            account.update();
            long balance = account.getBalance();
            command.getSource().sendFeedback(new TextComponentTranslation("Balance: %s", balance), false);
            return 1;
        }).then(Commands.argument("target", EntityArgument.singlePlayer()).executes((command) -> {
            EntityPlayer target = EntityArgument.getOnePlayer(command, "target");
            Account pAccount = Account.get(target);
            pAccount.update();
            command.getSource().sendFeedback(new TextComponentTranslation("%s balance: %s", target.getName(), pAccount.getBalance()), false);
            return 1;
        })));
        walletCommand.then(Commands.literal("set")
                .then(Commands.argument("target", EntityArgument.singlePlayer())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
            EntityPlayer target = EntityArgument.getOnePlayer(command, "target");
            Account pAccount = Account.get(target);
            pAccount.setBalance(IntegerArgumentType.getInteger(command, "amount"), false);
            command.getSource().sendFeedback(new TextComponentTranslation("%s balance set to %s", target.getName(), pAccount.getBalance()), false);
            return 1;
        }))));
        walletCommand.then(Commands.literal("give")
                .then(Commands.argument("target", EntityArgument.singlePlayer())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    EntityPlayer target = EntityArgument.getOnePlayer(command, "target");
                    Account pAccount = Account.get(target);
                    pAccount.addBalance(IntegerArgumentType.getInteger(command, "amount"), false);
                    command.getSource().sendFeedback(new TextComponentTranslation("%s balance is now %s", target.getName(), pAccount.getBalance()), false);
                    return 1;
                }))));
        walletCommand.then(Commands.literal("take")
                .then(Commands.argument("target", EntityArgument.singlePlayer())
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                .executes((command) -> {
                    EntityPlayer target = EntityArgument.getOnePlayer(command, "target");
                    Account pAccount = Account.get(target);
                    pAccount.addBalance(-IntegerArgumentType.getInteger(command, "amount"), false);
                    command.getSource().sendFeedback(new TextComponentTranslation("%s balance is now %s", target.getName(), pAccount.getBalance()), false);
                    return 1;
                }))));

        commandDispatcher.register(walletCommand);
    }
}
