package dev.the_fireplace.grandeconomy.events;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.CurrencyAPI;
import dev.the_fireplace.grandeconomy.config.ModConfig;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class KillingEvents {
    public static void onPlayerDeath(ServerPlayerEntity dyingPlayer, DamageSource source) {
        if (!dyingPlayer.world.isClient()) {
            double flatMoneyTransferAmount = ModConfig.getData().getPvpMoneyTransferFlat();
            double percentMoneyTransferAmount = ModConfig.getData().getPvpMoneyTransferPercent();
            if (doubleEquals(flatMoneyTransferAmount, 0) && doubleEquals(percentMoneyTransferAmount, 0))
                return;

            if (!(source.getAttacker() instanceof ServerPlayerEntity))
                return;
            ServerPlayerEntity killer = (ServerPlayerEntity) source.getAttacker();

            CurrencyAPI currencyAPI = CurrencyAPI.getInstance();
            double dyingPlayerBalance = currencyAPI.getBalance(dyingPlayer.getUuid(), true);
            if (dyingPlayerBalance < 0.01) {
                return;
            }
            double amountTaken = constrain(
                (dyingPlayerBalance * percentMoneyTransferAmount) / 100 + flatMoneyTransferAmount,
                0,
                dyingPlayerBalance
            );
            currencyAPI.takeFromBalance(dyingPlayer.getUuid(), amountTaken, true);
            GrandEconomy.getFeedbackSender().basic(dyingPlayer, "grandeconomy.killed_balance", currencyAPI.getFormattedBalance(dyingPlayer.getUuid(), true));

            currencyAPI.addToBalance(killer.getUuid(), amountTaken, true);
            GrandEconomy.getFeedbackSender().basic(killer, "grandeconomy.killer_balance", currencyAPI.getFormattedBalance(killer.getUuid(), true));
        }
    }

    private static boolean doubleEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }

    private static double constrain(double input, double min, double max) {
        return Math.max(Math.min(input, max), min);
    }
}
