package the_fireplace.grandeconomy.forge.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.forge.Config;
import the_fireplace.grandeconomy.forge.translation.TranslationUtil;

@Mod.EventBusSubscriber(modid=GrandEconomyApi.MODID)
public class KillingEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!event.getEntity().world.isRemote) {
            double moneyDropValue = Config.pvpMoneyTransfer;
            if (moneyDropValue == 0) return;
            Entity entity = event.getEntity();
            if (!(entity instanceof PlayerEntity) || entity.world.isRemote)
                return;
            Entity killer = event.getSource().getTrueSource();
            if (!(killer instanceof ServerPlayerEntity))
                return;

            if (GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) <= 0)
                return;
            double amountTaken = (moneyDropValue > 0) ?
                    (GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) * Config.pvpMoneyTransfer) / 100 :
                    Math.max(Math.min(GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true), -Config.pvpMoneyTransfer), 0);
            GrandEconomyApi.takeFromBalance(event.getEntity().getUniqueID(), amountTaken, true);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUniqueID(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true)));

            GrandEconomyApi.addToBalance(killer.getUniqueID(), amountTaken, true);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUniqueID(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUniqueID(), true)));
        }
    }
}
