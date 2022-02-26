package dev.the_fireplace.grandeconomy.eventhandlers;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PlayerDiedHandler
{

    private final ConfigValues configValues;
    private final Economy economy;
    private final FeedbackSender feedbackSender;

    @Inject
    public PlayerDiedHandler(ConfigValues configValues, Economy economy, TranslatorFactory translatorFactory, FeedbackSenderFactory feedbackSenderFactory) {
        this.configValues = configValues;
        this.economy = economy;
        this.feedbackSender = feedbackSenderFactory.get(translatorFactory.getTranslator(GrandEconomyConstants.MODID));
    }

    public void onPlayerDeath(ServerPlayer dyingPlayer, DamageSource source) {
        if (!dyingPlayer.level.isClientSide()) {
            double flatMoneyTransferAmount = configValues.getPvpMoneyTransferFlat();
            double percentMoneyTransferAmount = configValues.getPvpMoneyTransferPercent();
            if (doubleEquals(flatMoneyTransferAmount, 0) && doubleEquals(percentMoneyTransferAmount, 0)) {
                return;
            }

            if (!(source.getEntity() instanceof ServerPlayer)) {
                return;
            }
            ServerPlayer killer = (ServerPlayer) source.getEntity();

            double dyingPlayerBalance = economy.getBalance(dyingPlayer.getUUID(), true);
            if (dyingPlayerBalance < 0.01) {
                return;
            }
            double amountTaken = constrain(
                (dyingPlayerBalance * percentMoneyTransferAmount) / 100 + flatMoneyTransferAmount,
                0,
                dyingPlayerBalance
            );
            economy.takeFromBalance(dyingPlayer.getUUID(), amountTaken, true);
            feedbackSender.basic(dyingPlayer, "grandeconomy.killed_balance", economy.getFormattedBalance(dyingPlayer.getUUID(), true));

            economy.addToBalance(killer.getUUID(), amountTaken, true);
            feedbackSender.basic(killer, "grandeconomy.killer_balance", economy.getFormattedBalance(killer.getUUID(), true));
        }
    }

    private static boolean doubleEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0001;
    }

    private static double constrain(double input, double min, double max) {
        return Math.max(Math.min(input, max), min);
    }
}
