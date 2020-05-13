package the_fireplace.grandeconomy.forge.events;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.forge.Config;
import the_fireplace.grandeconomy.forge.translation.TranslationUtil;

import java.util.Objects;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

@Mod.EventBusSubscriber(modid=GrandEconomyApi.MODID)
public class NetworkEvents {
    private static final ResourceLocation channelName = new ResourceLocation(GrandEconomyApi.MODID, "network");
    private static final String protocolId = "1";

    public static void init(){
        NetworkRegistry.newEventChannel(channelName, () -> protocolId, protocolServer -> true, protocolClient -> true).registerObject(new NetworkEvents());
    }

    @SubscribeEvent
    public void onClientToServerPacket(NetworkEvent.ClientCustomPayloadEvent e){
        NetworkEvent.Context ctx = e.getSource().get();
        TranslationUtil.geClients.add(Objects.requireNonNull(ctx.getSender()).getUniqueID());
        ctx.setPacketHandled(true);
    }

    public static PacketBuffer buf(){
        return new PacketBuffer(Unpooled.buffer());
    }

    public static void sendToServer(PacketBuffer buffer){
        PacketDistributor.SERVER.noArg().send(PLAY_TO_SERVER.buildPacket(Pair.of(buffer, 0), channelName).getThis());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientConnect(ClientPlayerNetworkEvent.LoggedInEvent event) {
        sendToServer(buf());
    }

    @SubscribeEvent
    public static void playerDisconnected(PlayerEvent.PlayerLoggedOutEvent event) {
        TranslationUtil.geClients.remove(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getEntity().world.isRemote) {
            if(Config.showBalanceOnJoin)
                event.getEntity().sendMessage(TranslationUtil.getTranslation(event.getEntity().getUniqueID(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(event.getEntity().getUniqueID(), true)));
        }
    }
}
