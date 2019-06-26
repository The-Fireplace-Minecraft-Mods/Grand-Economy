package the_fireplace.grandeconomy.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.earnings.ConversionItems;
import the_fireplace.grandeconomy.econhandlers.ge.Account;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandConvert extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "convert";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return "/convert";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            ResourceLocation heldResource = ((EntityPlayer) sender).getHeldItemMainhand().getItem().getRegistryName();
            int heldMeta = ((EntityPlayer) sender).getHeldItemMainhand().getMetadata();
            if(ConversionItems.hasValue(heldResource, heldMeta)) {
                int value = ConversionItems.getValue(heldResource, heldMeta);
                int count = ((EntityPlayer) sender).getHeldItemMainhand().getCount();
                Account.get((EntityPlayer) sender).addBalance(value * count, false);
                ((EntityPlayer) sender).setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                sender.sendMessage(new TextComponentTranslation("You have exchanged %s %s for %s each, earning a total of %s. Your balance is now %s.", count, heldResource.toString(), value, value * count, GrandEconomyApi.getBalance(((EntityPlayer)sender).getUniqueID())));
            } else
                sender.sendMessage(new TextComponentString("The item you are holding has no value."));
            return;
        }
        throw new WrongUsageException(getUsage(sender));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }
}
