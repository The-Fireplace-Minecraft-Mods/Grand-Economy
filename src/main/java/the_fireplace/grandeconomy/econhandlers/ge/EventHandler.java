package the_fireplace.grandeconomy.econhandlers.ge;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.TimeUtils;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import java.io.IOException;

public class EventHandler {
    private static long lastTickEvent = 0;
    private static long saveTimer = 0;

    @SubscribeEvent
    public void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        Account account = Account.get(event.getEntityPlayer());
        try {
            account.writeIfChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if(saveTimer++ >= 20*60*5) {
            saveTimer -= 20*60*5;
            Account.saveAll();
        }
        long now = TimeUtils.getCurrentDay();
        if (lastTickEvent == now)
            return;
        lastTickEvent = now;
        MinecraftServer server = GrandEconomy.minecraftServer;
        if (server == null)
            return;
        for (EntityPlayerMP entityPlayer : server.getPlayerList().getPlayers()) {
            Account account = Account.get(entityPlayer);
            if (account.update())
                entityPlayer.sendMessage(TranslationUtil.getTranslation(entityPlayer.getUniqueID(), "commands.grandeconomy.common.balance", account.getBalance()));
        }
    }
}
