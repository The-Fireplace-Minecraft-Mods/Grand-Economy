package the_fireplace.grandeconomy.fabric.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.fabric.Config;

public class KillingEvents {
    public static void onPlayerDeath(ServerPlayerEntity entity, DamageSource source) {
        if(!entity.world.isClient()) {
            int moneyDropValue = Config.pvpMoneyTransfer;
            if (moneyDropValue == 0) return;
            Entity killer = source.getAttacker();
            if (!(killer instanceof ServerPlayerEntity))
                return;

            if (GrandEconomyApi.getBalance(entity.getUuid(), true) <= 0)
                return;
            long amountTaken = (moneyDropValue > 0) ?
                (GrandEconomyApi.getBalance(entity.getUuid(), true) * Config.pvpMoneyTransfer) / 100 :
                Math.max(Math.min(GrandEconomyApi.getBalance(entity.getUuid(), true), -Config.pvpMoneyTransfer), 0);
            GrandEconomyApi.takeFromBalance(entity.getUuid(), amountTaken, true);

            entity.sendMessage(TranslationUtil.getTranslation(entity.getUniqueID(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(entity.getUuid(), true)));

            GrandEconomyApi.addToBalance(killer.getUuid(), amountTaken, true);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUniqueID(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUuid(), true)));
        }
    }
}
