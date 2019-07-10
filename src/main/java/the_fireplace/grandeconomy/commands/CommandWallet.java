package the_fireplace.grandeconomy.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

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
        return TranslationUtil.getRawTranslationString(sender, "commands.grandeconomy.wallet.usage");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 1) {
            EntityPlayerMP target = getPlayer(server, sender, args[1]);
            if ("balance".equals(args[0])) {
                sender.sendMessage(TranslationUtil.getTranslation(sender, "commands.grandeconomy.wallet.balance",
                        target.getName(), GrandEconomyApi.getBalance(target.getUniqueID())));
                return;
            }
            if(args.length > 2) {
                long amount = parseLong(args[2]);
                if ("set".equals(args[0])) {
                    GrandEconomyApi.setBalance(target.getUniqueID(), amount, true);
                    sender.sendMessage(TranslationUtil.getTranslation(sender, "commands.grandeconomy.wallet.set",
                            target.getName(), GrandEconomyApi.getBalance(target.getUniqueID())));
                    return;
                }
                if ("give".equals(args[0]) || "add".equals(args[0])) {
                    GrandEconomyApi.addToBalance(target.getUniqueID(), amount, true);
                    sender.sendMessage(TranslationUtil.getTranslation(sender, "commands.grandeconomy.wallet.given",
                            amount, target.getName()));
                    return;
                }
                if ("take".equals(args[0])) {
                    GrandEconomyApi.takeFromBalance(target.getUniqueID(), amount, true);
                    sender.sendMessage(TranslationUtil.getTranslation(sender, "commands.grandeconomy.wallet.taken",
                            amount, target.getName()));
                    return;
                }
            }
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
