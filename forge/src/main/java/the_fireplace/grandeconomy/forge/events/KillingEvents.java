package the_fireplace.grandeconomy.forge.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_fireplace.grandeconomy.api.IGrandEconomyApi;
import the_fireplace.grandeconomy.forge.Config;
import the_fireplace.grandeconomy.forge.GrandEconomy;
import the_fireplace.grandeconomy.forge.translation.TranslationUtil;

@Mod.EventBusSubscriber(modid= GrandEconomy.MODID)
public class KillingEvents {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if(!event.getEntity().world.isRemote) {
            int moneyDropValue = Config.pvpMoneyTransfer;
            if (moneyDropValue == 0) return;
            Entity entity = event.getEntity();
            if (!(entity instanceof PlayerEntity) || entity.world.isRemote)
                return;
            Entity killer = event.getSource().getTrueSource();
            if (!(killer instanceof ServerPlayerEntity))
                return;

            if (IGrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) <= 0)
                return;
            long amountTaken = (moneyDropValue > 0) ?
                    (IGrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true) * Config.pvpMoneyTransfer) / 100 :
                    Math.max(Math.min(IGrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true), -Config.pvpMoneyTransfer), 0);
            IGrandEconomyApi.takeFromBalance(event.getEntity().getUniqueID(), amountTaken, true);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUniqueID(), "grandeconomy.killed_balance", IGrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true)));

            IGrandEconomyApi.addToBalance(killer.getUniqueID(), amountTaken, true);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUniqueID(), "grandeconomy.killer_balance", IGrandEconomyApi.getBalance(killer.getUniqueID(), true)));
        }
    }
}
