package the_fireplace.grandeconomy.forge.econhandlers.ge;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.forge.GrandEconomy;
import the_fireplace.grandeconomy.utils.TimeUtils;
import the_fireplace.grandeconomy.forge.translation.TranslationUtil;

import java.io.IOException;

public class EventHandler {
    private static long lastTickEvent = 0;

    @SubscribeEvent
    public void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        //TODO rewrite this, it can probably be made much more efficient
        long now = TimeUtils.getCurrentDay();
        if (lastTickEvent == now)
            return;
        lastTickEvent = now;
        MinecraftServer server = GrandEconomy.getServer();
        //noinspection ConstantConditions
        if (server == null)
            return;
        for (ServerPlayerEntity playerEntity : server.getPlayerList().getPlayers()) {
            Account account = Account.get(playerEntity);
            if (account.update())
                playerEntity.sendMessage(TranslationUtil.getTranslation(playerEntity.getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.formatCurrency(account.getBalance())));
        }
    }
}
