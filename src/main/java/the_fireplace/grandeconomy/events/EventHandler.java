package the_fireplace.grandeconomy.events;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.Utils;
import the_fireplace.grandeconomy.economy.Account;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = GrandEconomy.MODID)
public class EventHandler {
    private static long lastTickEvent = 0;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
            Account account = Account.get((EntityPlayer) event.getEntity());
            account.update();
            long balance = account.getBalance();
            event.getEntity().sendMessage(new TextComponentTranslation("Balance: %s", balance));
        }
    }

    @SubscribeEvent
    public static void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getEntityPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        long now = Utils.getCurrentDay();
        if (lastTickEvent == now)
            return;
        lastTickEvent = now;
        MinecraftServer server = GrandEconomy.minecraftServer;
        if (server == null)
            return;
        for (EntityPlayerMP entityPlayer : server.getPlayerList().getPlayers()) {
            Account account = Account.get(entityPlayer);
            if (account.update())
                entityPlayer.sendMessage(new TextComponentTranslation("Balance: %s", account.getBalance()));
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        int moneyDropValue = GrandEconomy.settings.getPvpMoneyDrop();
        if (moneyDropValue == 0) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer) || entity.world.isRemote)
            return;
        Entity killer = event.getSource().getTrueSource();
        if (!(killer instanceof EntityPlayerMP))
            return;

        Account account = Account.get((EntityPlayer) entity);
        if (account.getBalance() <= 0)
            return;
        long amountTaken = (moneyDropValue > 0) ?
                (account.getBalance() * GrandEconomy.settings.getPvpMoneyDrop()) / 100 :
                Math.max(Math.min(account.getBalance(), -GrandEconomy.settings.getPvpMoneyDrop()), 0);
        account.addBalance(-amountTaken);

        long balance = account.getBalance();
        entity.sendMessage(new TextComponentTranslation("Balance: %s", balance));

        Account killerAccount = Account.get((EntityPlayer) killer);
        killerAccount.addBalance(amountTaken);
        long balance2 = killerAccount.getBalance();
        killer.sendMessage(new TextComponentTranslation("Balance: %s", balance2));
    }
}
