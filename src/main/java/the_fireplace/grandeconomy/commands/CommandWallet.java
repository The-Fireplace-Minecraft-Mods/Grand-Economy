package the_fireplace.grandeconomy.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.grandeconomy.econhandlers.ge.Account;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandWallet extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "wallet";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "/wallet <give|take|set|balance> <player> <amount>";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 1) {
            EntityPlayerMP entityplayer = getPlayer(server, sender, args[1]);
            //noinspection ConstantConditions
            if(entityplayer == null)
                throw new WrongUsageException(getUsage(sender));
            Account account = Account.get(entityplayer);
            account.update();
            if ("balance".equals(args[0])) {
                sender.sendMessage((new TextComponentTranslation("%s balance: %s",
                        entityplayer.getName(), account.getBalance())));
                return;
            }
            long amount = parseLong(args[2]);
            if ("set".equals(args[0])) {
                account.setBalance(amount, false);
                sender.sendMessage((new TextComponentTranslation("%s balance set to %s",
                        entityplayer.getName(), account.getBalance())));
                return;
            }
            if ("give".equals(args[0]) || "add".equals(args[0])) {
                account.addBalance(amount, false);
                sender.sendMessage((new TextComponentTranslation("%s added to %s balance",
                        amount, entityplayer.getName())));
                return;
            }
            if ("take".equals(args[0])) {
                account.addBalance(-amount, false);
                sender.sendMessage((new TextComponentTranslation("%s taken from %s balance",
                        amount, entityplayer.getName())));
                return;
            }
            entityplayer.sendMessage(new TextComponentTranslation("Balance: %s", account.getBalance()));
        }
        throw new WrongUsageException(getUsage(sender));
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "give", "take", "set", "balance", "add");
        }
        if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
