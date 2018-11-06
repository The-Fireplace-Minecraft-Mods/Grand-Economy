package the_fireplace.grandeconomy.commands;

import the_fireplace.grandeconomy.economy.Account;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandBalance extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "balance";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "/balance";
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nullable String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            //noinspection RedundantArrayCreation
            Account account = Account.get((EntityPlayer) sender);
            account.update();
            long balance = account.getBalance();
            notifyCommandListener(sender, this, "Balance: %s", balance);
            return;
        }
        //noinspection RedundantArrayCreation
        throw new WrongUsageException("/balance", new Object[0]);
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
