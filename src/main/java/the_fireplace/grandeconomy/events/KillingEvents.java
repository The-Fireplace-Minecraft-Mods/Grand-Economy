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
            double flatMoneyTransferAmount = GrandEconomy.globalConfig.pvpMoneyTransferFlat;
            double percentMoneyTransferAmount = GrandEconomy.globalConfig.pvpMoneyTransferPercent;
            if (flatMoneyTransferAmount == 0 && percentMoneyTransferAmount == 0) return;
            Entity entity = event.getEntity();
            if (!(entity instanceof EntityPlayer) || entity.world.isRemote)
                return;
            Entity killer = event.getSource().getTrueSource();
            if (!(killer instanceof EntityPlayerMP))
                return;

            if (GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) <= 0)
                return;
            double amountTaken =
                    Math.max(
                        Math.min(
                            GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true),
                            (GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) * percentMoneyTransferAmount) / 100 + flatMoneyTransferAmount),
                        0
                    );
            GrandEconomyApi.takeFromBalance(event.getEntity().getUniqueID(), amountTaken, true);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUniqueID(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true)));

            GrandEconomyApi.addToBalance(killer.getUniqueID(), amountTaken, true);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUniqueID(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUniqueID(), true)));
        }
    }
}
