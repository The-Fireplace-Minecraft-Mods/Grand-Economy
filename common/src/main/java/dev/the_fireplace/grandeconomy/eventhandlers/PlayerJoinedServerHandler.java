package dev.the_fireplace.grandeconomy.eventhandlers;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.logintracker.PlayerLoginTracker;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import javax.inject.Inject;

public final class PlayerJoinedServerHandler
{

    private final PlayerLoginTracker playerLoginTracker;
    private final Economy economy;
    private final Translator translator;
    private final ConfigValues configValues;

    @Inject
    public PlayerJoinedServerHandler(PlayerLoginTracker playerLoginTracker, Economy economy, TranslatorFactory translatorFactory, ConfigValues configValues) {
        this.playerLoginTracker = playerLoginTracker;
        this.economy = economy;
        this.translator = translatorFactory.getTranslator(GrandEconomyConstants.MODID);
        this.configValues = configValues;
    }

    public void onPlayerJoinServer(ServerPlayer player) {
        playerLoginTracker.addLogin(player.getUUID());
        if (configValues.isShowBalanceOnJoin()) {
            Component joinMessage = translator.getTextForTarget(
                player.getUUID(),
                CommonTranslationKeys.BALANCE,
                economy.getFormattedBalance(player.getUUID(), true)
            );
            player.displayClientMessage(joinMessage, true);
        }
    }
}
