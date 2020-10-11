package the_fireplace.grandeconomy.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.translation.TranslationUtil;

public class KillingEvents {
    public static void onPlayerDeath(ServerPlayerEntity entity, DamageSource source) {
        if(!entity.world.isClient()) {
            double moneyDropValue = ModConfig.pvpMoneyTransfer;
            if (moneyDropValue == 0) return;
            Entity killer = source.getAttacker();
            if (!(killer instanceof ServerPlayerEntity))
                return;

            if (GrandEconomyApi.getBalance(entity.getUuid(), true) <= 0)
                return;
            double amountTaken = (moneyDropValue > 0) ?
                (GrandEconomyApi.getBalance(entity.getUuid(), true) * ModConfig.pvpMoneyTransfer) / 100 :
                Math.max(Math.min(GrandEconomyApi.getBalance(entity.getUuid(), true), -ModConfig.pvpMoneyTransfer), 0);
            GrandEconomyApi.takeFromBalance(entity.getUuid(), amountTaken, true);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUuid(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(entity.getUuid(), true)), false);

            GrandEconomyApi.addToBalance(killer.getUuid(), amountTaken, true);
            ((ServerPlayerEntity) killer).sendMessage(TranslationUtil.getTranslation(killer.getUuid(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUuid(), true)), false);
        }
    }
}
