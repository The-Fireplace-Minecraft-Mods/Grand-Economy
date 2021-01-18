package the_fireplace.grandeconomy.events;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.logintracker.LoginTracker;

public class NetworkEvents {
    public static void onPlayerJoinServer(ServerPlayerEntity player) {
        LoginTracker.get(player.getUuid()).addLogin();
        if (GrandEconomy.config.showBalanceOnJoin) {
            Text joinMessage = GrandEconomy.getTranslator().getTextForTarget(
                player.getUuid(),
                "commands.grandeconomy.common.balance",
                GrandEconomyApi.getBalanceFormatted(player.getUuid(), true)
            );
            player.sendMessage(joinMessage, false);
        }
    }
}
