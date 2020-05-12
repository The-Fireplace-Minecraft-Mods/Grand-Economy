package the_fireplace.grandeconomy.commands;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.econhandlers.ge.InsufficientCreditException;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandPay extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "pay";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.grandeconomy.pay.usage");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 2) {
            if(sender instanceof EntityPlayerMP) {
                EntityPlayerMP targetPlayer = getPlayer(server, sender, args[0]);
                double amount = parseDouble(args[1]);
                if (amount < 0)
                    throw new NumberInvalidException(TranslationUtil.getStringTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.grandeconomy.pay.negative"));
                if (GrandEconomyApi.getBalance(((EntityPlayerMP) sender).getUniqueID(), true) < amount)
                    throw new InsufficientCreditException(((EntityPlayerMP) sender).getUniqueID());
                GrandEconomyApi.takeFromBalance(((EntityPlayerMP) sender).getUniqueID(), amount, true);
                GrandEconomyApi.addToBalance(targetPlayer.getUniqueID(), amount, true);
                sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.grandeconomy.pay.paid", GrandEconomyApi.getFormattedCurrency(amount), targetPlayer.getName()));
                targetPlayer.sendMessage(TranslationUtil.getTranslation(targetPlayer.getUniqueID(), "commands.grandeconomy.pay.received", GrandEconomyApi.getFormattedCurrency(amount), sender.getName()));
            } else
                throw new WrongUsageException(TranslationUtil.getStringTranslation("commands.grandeconomy.common.console", getUsage(sender)));
        } else
            throw new WrongUsageException(getUsage(sender));
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }
}
