package dev.the_fireplace.grandeconomy.events;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.CommonTranslationKeys;
import dev.the_fireplace.grandeconomy.config.ModConfig;
import dev.the_fireplace.grandeconomy.logintracker.LoginTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class NetworkEvents {
    public static void onPlayerJoinServer(ServerPlayerEntity player) {
        LoginTracker.get(player.getUuid()).addLogin();
        if (ModConfig.getData().isShowBalanceOnJoin()) {
            Text joinMessage = GrandEconomy.getTranslator().getTextForTarget(
                player.getUuid(),
                CommonTranslationKeys.BALANCE,
                CurrencyAPI.getInstance().getFormattedBalance(player.getUuid(), true)
            );
            player.sendMessage(joinMessage, false);
        }
    }
}
