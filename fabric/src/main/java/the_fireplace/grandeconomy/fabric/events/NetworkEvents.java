package the_fireplace.grandeconomy.fabric.events;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.fabric.Config;
import the_fireplace.grandeconomy.fabric.translation.TranslationUtil;

import java.util.Objects;
import java.util.UUID;

public class NetworkEvents {
    private static final Identifier clientConnectedPacketId = new Identifier(GrandEconomyApi.MODID, "client_connected");
    
    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(clientConnectedPacketId, (ctx, attachedData) -> {
            TranslationUtil.geClients.add(Objects.requireNonNull(ctx.getPlayer()).getUuid());
        });
    }

    public static void onDisconnected(UUID player) {
        TranslationUtil.geClients.remove(player);
    }

    public static PacketByteBuf buf(){
        return new PacketByteBuf(Unpooled.buffer());
    }

    @Environment(EnvType.CLIENT)
    public static void onConnectToServer() {
        ClientSidePacketRegistry.INSTANCE.sendToServer(clientConnectedPacketId, buf());
    }
    
    public static void onPlayerJoinServer(ServerPlayerEntity player) {
        GrandEconomyApi.ensureAccountExists(player.getUuid(), true);
        if(Config.showBalanceOnJoin)
            player.sendMessage(TranslationUtil.getTranslation(player.getUuid(), "commands.grandeconomy.common.balance", GrandEconomyApi.getBalanceFormatted(player.getUuid(), true)));
    }
}
