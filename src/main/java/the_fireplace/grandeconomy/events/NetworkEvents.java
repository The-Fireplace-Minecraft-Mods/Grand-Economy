package the_fireplace.grandeconomy.events;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;
import the_fireplace.grandeconomy.Config;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Mod.EventBusSubscriber(modid=GrandEconomy.MODID)
public class NetworkEvents {
    //Use a message that is processed as invalid by the server so it won't get displayed even if Clans is not loaded
    private static final String GRANDECONOMY_VERIFICATION_MESSAGE = "Grand Economy loaded on client";

    @SubscribeEvent
    public static void packetRecieved(NetworkEvent.ServerCustomPayloadEvent event) {
        if(event.getPayload().asReadOnly().capacity() >= GRANDECONOMY_VERIFICATION_MESSAGE.getBytes().length)
            System.out.println(event.getPayload().asReadOnly().readCharSequence(GRANDECONOMY_VERIFICATION_MESSAGE.getBytes().length, StandardCharsets.UTF_8));
        if(event.getPayload().asReadOnly().capacity() >= GRANDECONOMY_VERIFICATION_MESSAGE.getBytes().length && event.getPayload().asReadOnly().readCharSequence(GRANDECONOMY_VERIFICATION_MESSAGE.getBytes().length, StandardCharsets.UTF_8).equals(GRANDECONOMY_VERIFICATION_MESSAGE)) {
            TranslationUtil.geClients.add(Objects.requireNonNull(event.getSource().get().getSender()).getUniqueID());
            event.getSource().get().setPacketHandled(true);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientConnect(ClientPlayerNetworkEvent.LoggedInEvent event) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeCharSequence(GRANDECONOMY_VERIFICATION_MESSAGE, StandardCharsets.UTF_8);
        Objects.requireNonNull(event.getPlayer()).connection.sendPacket(new CCustomPayloadPacket(new ResourceLocation("grandeconomy", "ge"), new PacketBuffer(buf)));
    }

    @SubscribeEvent
    public static void playerDisconnected(PlayerEvent.PlayerLoggedOutEvent event) {
        TranslationUtil.geClients.remove(event.getPlayer().getUniqueID());
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
