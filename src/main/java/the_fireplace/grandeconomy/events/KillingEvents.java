package the_fireplace.grandeconomy.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

@Mod.EventBusSubscriber(modid=GrandEconomy.MODID)
public class KillingEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!event.getEntity().world.isRemote) {
            int moneyDropValue = GrandEconomy.cfg.pvpMoneyTransfer;
            if (moneyDropValue == 0) return;
            Entity entity = event.getEntity();
            if (!(entity instanceof EntityPlayer) || entity.world.isRemote)
                return;
            Entity killer = event.getSource().getTrueSource();
            if (!(killer instanceof EntityPlayerMP))
                return;

            if (GrandEconomyApi.getBalance(event.getEntity().getUniqueID()) <= 0)
                return;
            long amountTaken = (moneyDropValue > 0) ?
                    (GrandEconomyApi.getBalance(event.getEntity().getUniqueID()) * GrandEconomy.cfg.pvpMoneyTransfer) / 100 :
                    Math.max(Math.min(GrandEconomyApi.getBalance(event.getEntity().getUniqueID()), -GrandEconomy.cfg.pvpMoneyTransfer), 0);
            GrandEconomyApi.takeFromBalance(event.getEntity().getUniqueID(), amountTaken, false);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUniqueID(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(event.getEntity().getUniqueID())));

            GrandEconomyApi.addToBalance(killer.getUniqueID(), amountTaken, false);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUniqueID(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUniqueID())));
        }
    }
}
