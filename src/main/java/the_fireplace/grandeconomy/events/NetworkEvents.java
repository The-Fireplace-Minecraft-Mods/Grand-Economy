package the_fireplace.grandeconomy.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;
import the_fireplace.grandeconomy.Config;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

@Mod.EventBusSubscriber(modid=GrandEconomy.MODID)
public class NetworkEvents {

    @SubscribeEvent
    public static void clientConnectToServer(NetworkEvent.LoginPayloadEvent event) {
        ;
        //TranslationUtil.geClients.add(event.);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote) {
            GrandEconomyApi.ensureAccountExists(event.getEntity().getUniqueID(), true);
            if(Config.showBalanceOnJoin)
                event.getEntity().sendMessage(TranslationUtil.getTranslation(event.getEntity().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalance(event.getEntity().getUniqueID(), true)));
        }
    }
}
