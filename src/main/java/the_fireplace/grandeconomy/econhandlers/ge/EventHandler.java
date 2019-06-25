package the_fireplace.grandeconomy.econhandlers.ge;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class EventHandler {
    private static long lastTickEvent = 0;

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
            Account account = Account.get((EntityPlayer) event.getEntity());
            account.update();
            long balance = account.getBalance();
            if(GrandEconomy.cfg.showBalanceOnJoin)
                event.getEntity().sendMessage(new TextComponentTranslation("Balance: %s", balance));
        }
    }

    @SubscribeEvent
    public void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getEntityPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
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
    public void onLivingDeathEvent(LivingDeathEvent event) {
        if(!event.getEntity().world.isRemote) {
            int moneyDropValue = GrandEconomy.cfg.pvpMoneyTransfer;
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
                    (account.getBalance() * GrandEconomy.cfg.pvpMoneyTransfer) / 100 :
                    Math.max(Math.min(account.getBalance(), -GrandEconomy.cfg.pvpMoneyTransfer), 0);
            account.addBalance(-amountTaken, false);

            long balance = account.getBalance();
            entity.sendMessage(new TextComponentTranslation("You were killed, your balance is now: %s", balance));

            Account killerAccount = Account.get((EntityPlayer) killer);
            killerAccount.addBalance(amountTaken, false);
            long balance2 = killerAccount.getBalance();
            killer.sendMessage(new TextComponentTranslation("You just killed someone, your balance is now: %s", balance2));
        }
    }
}
