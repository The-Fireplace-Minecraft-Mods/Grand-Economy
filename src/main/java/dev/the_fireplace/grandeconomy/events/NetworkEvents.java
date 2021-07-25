package dev.the_fireplace.grandeconomy.events;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.logintracker.PlayerLoginTracker;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import javax.inject.Inject;

public final class NetworkEvents {

    private final PlayerLoginTracker playerLoginTracker;
    private final CurrencyAPI currencyAPI;
    private final Translator translator;
    private final ConfigValues configValues;

    @Inject
    public NetworkEvents(PlayerLoginTracker playerLoginTracker, CurrencyAPI currencyAPI, TranslatorFactory translatorFactory, ConfigValues configValues) {
        this.playerLoginTracker = playerLoginTracker;
        this.currencyAPI = currencyAPI;
        this.translator = translatorFactory.getTranslator(GrandEconomyConstants.MODID);
        this.configValues = configValues;
    }

    public void onPlayerJoinServer(ServerPlayerEntity player) {
        playerLoginTracker.addLogin(player.getUuid());
        if (configValues.isShowBalanceOnJoin()) {
            Text joinMessage = translator.getTextForTarget(
                player.getUuid(),
                CommonTranslationKeys.BALANCE,
                currencyAPI.getFormattedBalance(player.getUuid(), true)
            );
            player.sendMessage(joinMessage);
        }
    }
}
