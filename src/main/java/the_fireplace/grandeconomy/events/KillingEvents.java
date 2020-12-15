package the_fireplace.grandeconomy.events;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

public class KillingEvents {
    public static void onPlayerDeath(ServerPlayerEntity dyingPlayer, DamageSource source) {
        if(!dyingPlayer.world.isClient()) {
            double flatMoneyTransferAmount = GrandEconomy.config.pvpMoneyTransferFlat;
            double percentMoneyTransferAmount = GrandEconomy.config.pvpMoneyTransferPercent;
            if (flatMoneyTransferAmount == 0 && percentMoneyTransferAmount == 0)
                return;

            if (!(source.getAttacker() instanceof ServerPlayerEntity))
                return;
            ServerPlayerEntity killer = (ServerPlayerEntity) source.getAttacker();

            if (GrandEconomyApi.getBalance(dyingPlayer.getUuid(), true) <= 0)
                return;
            double amountTaken =
                Math.max(
                    Math.min(
                        GrandEconomyApi.getBalance(dyingPlayer.getUuid(), true),
                        (GrandEconomyApi.getBalance(dyingPlayer.getUuid(), true) * percentMoneyTransferAmount) / 100 + flatMoneyTransferAmount),
                    0
                );
            GrandEconomyApi.takeFromBalance(dyingPlayer.getUuid(), amountTaken, true);

            dyingPlayer.sendMessage(TranslationUtil.getTranslation(dyingPlayer.getUuid(), "grandeconomy.killed_balance", GrandEconomyApi.getBalance(dyingPlayer.getUuid(), true)), false);

            GrandEconomyApi.addToBalance(killer.getUuid(), amountTaken, true);
            killer.sendMessage(TranslationUtil.getTranslation(killer.getUuid(), "grandeconomy.killer_balance", GrandEconomyApi.getBalance(killer.getUuid(), true)), false);
        }
    }
}
