package the_fireplace.grandeconomy.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

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
        return TranslationUtil.getRawTranslationString(sender, "commands.grandeconomy.balance.usage");
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nullable String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayer) sender).getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(((EntityPlayer) sender).getUniqueID(), true)));
            return;
        }
        throw new WrongUsageException(TranslationUtil.getStringTranslation("commands.grandeconomy.common.console", getUsage(sender)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }
}
