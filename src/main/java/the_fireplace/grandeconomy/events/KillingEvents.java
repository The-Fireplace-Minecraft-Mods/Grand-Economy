package the_fireplace.grandeconomy.events;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.CurrencyAPI;

public class KillingEvents {
    public static void onPlayerDeath(ServerPlayerEntity dyingPlayer, DamageSource source) {
        if(!dyingPlayer.world.isClient()) {
            double flatMoneyTransferAmount = GrandEconomy.config.pvpMoneyTransferFlat;
            double percentMoneyTransferAmount = GrandEconomy.config.pvpMoneyTransferPercent;
            if (doubleEquals(flatMoneyTransferAmount, 0) && doubleEquals(percentMoneyTransferAmount, 0))
                return;

            if (!(source.getAttacker() instanceof ServerPlayerEntity))
                return;
            ServerPlayerEntity killer = (ServerPlayerEntity) source.getAttacker();

            if (CurrencyAPI.getInstance().getBalance(dyingPlayer.getUuid(), true) < 1)
                return;
            double amountTaken = constrain(
                (CurrencyAPI.getInstance().getBalance(dyingPlayer.getUuid(), true) * percentMoneyTransferAmount) / 100 + flatMoneyTransferAmount,
                0,
                CurrencyAPI.getInstance().getBalance(dyingPlayer.getUuid(), true)
            );
            CurrencyAPI.getInstance().takeFromBalance(dyingPlayer.getUuid(), amountTaken, true);
            dyingPlayer.sendMessage(GrandEconomy.getTranslator().getTextForTarget(dyingPlayer.getUuid(), "grandeconomy.killed_balance", CurrencyAPI.getInstance().getBalanceFormatted(dyingPlayer.getUuid(), true)), false);

            CurrencyAPI.getInstance().addToBalance(killer.getUuid(), amountTaken, true);
            killer.sendMessage(GrandEconomy.getTranslator().getTextForTarget(killer.getUuid(), "grandeconomy.killer_balance", CurrencyAPI.getInstance().getBalanceFormatted(killer.getUuid(), true)), false);
        }
    }

    private static boolean doubleEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }

    private static double constrain(double input, double min, double max) {
        return Math.max(Math.min(input, max), min);
    }
}
