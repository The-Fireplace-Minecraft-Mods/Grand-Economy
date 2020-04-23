package the_fireplace.grandeconomy.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import java.util.Map;

@Mod.EventBusSubscriber(modid=GrandEconomy.MODID)
public class NetworkEvents {

    @SubscribeEvent
    public static void clientConnectToServer(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        Map<String, String> clientMods = NetworkDispatcher.get(event.getManager()).getModList();
        if(event.getHandler() instanceof NetHandlerPlayServer && ((NetHandlerPlayServer) event.getHandler()).player != null && clientMods.containsKey(GrandEconomy.MODID) && !clientMods.get(GrandEconomy.MODID).startsWith("1.0.") && !clientMods.get(GrandEconomy.MODID).startsWith("1.1."))
            TranslationUtil.geClients.add(((NetHandlerPlayServer) event.getHandler()).player.getUniqueID());
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
            GrandEconomyApi.ensureAccountExists(event.getEntity().getUniqueID(), true);
            if(GrandEconomy.cfg.showBalanceOnJoin)
                event.getEntity().sendMessage(TranslationUtil.getTranslation(event.getEntity().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(event.getEntity().getUniqueID(), true)));
        }
    }
}
