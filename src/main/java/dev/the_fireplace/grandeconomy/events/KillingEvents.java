package dev.the_fireplace.grandeconomy.events;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class KillingEvents {
    
    private final ConfigValues configValues;
    private final CurrencyAPI currencyAPI;
    private final FeedbackSender feedbackSender;

    @Inject
    public KillingEvents(ConfigValues configValues, CurrencyAPI currencyAPI, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory) {
        this.configValues = configValues;
        this.currencyAPI = currencyAPI;
        this.feedbackSender = feedbackSenderFactory.get(translatorFactory.getTranslator(GrandEconomyConstants.MODID));
    }

    public void onPlayerDeath(ServerPlayerEntity dyingPlayer, DamageSource source) {
        if (!dyingPlayer.world.isClient()) {
            double flatMoneyTransferAmount = configValues.getPvpMoneyTransferFlat();
            double percentMoneyTransferAmount = configValues.getPvpMoneyTransferPercent();
            if (doubleEquals(flatMoneyTransferAmount, 0) && doubleEquals(percentMoneyTransferAmount, 0))
                return;

            if (!(source.getAttacker() instanceof ServerPlayerEntity))
                return;
            ServerPlayerEntity killer = (ServerPlayerEntity) source.getAttacker();

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
            feedbackSender.basic(dyingPlayer, "grandeconomy.killed_balance", currencyAPI.getFormattedBalance(dyingPlayer.getUuid(), true));

            currencyAPI.addToBalance(killer.getUuid(), amountTaken, true);
            feedbackSender.basic(killer, "grandeconomy.killer_balance", currencyAPI.getFormattedBalance(killer.getUuid(), true));
        }
    }

    private static boolean doubleEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }

    private static double constrain(double input, double min, double max) {
        return Math.max(Math.min(input, max), min);
    }
}
