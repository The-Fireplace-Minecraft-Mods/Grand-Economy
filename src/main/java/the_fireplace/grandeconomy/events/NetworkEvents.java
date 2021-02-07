package the_fireplace.grandeconomy.events;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.CurrencyAPI;
import the_fireplace.grandeconomy.command.CommonTranslationKeys;
import the_fireplace.grandeconomy.logintracker.LoginTracker;

public class NetworkEvents {
    public static void onPlayerJoinServer(ServerPlayerEntity player) {
        LoginTracker.get(player.getUuid()).addLogin();
        if (GrandEconomy.getConfig().showBalanceOnJoin) {
            Text joinMessage = GrandEconomy.getTranslator().getTextForTarget(
                player.getUuid(),
                CommonTranslationKeys.BALANCE,
                CurrencyAPI.getInstance().getFormattedBalance(player.getUuid(), true)
            );
            player.sendMessage(joinMessage, false);
        }
    }
}
