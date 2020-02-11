package the_fireplace.grandeconomy.fabric.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
@Environment(EnvType.CLIENT)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("RETURN"), method = "onGameJoin(Lnet/minecraft/client/network/packet/GameJoinS2CPacket;)V")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        the_fireplace.grandeconomy.fabric.events.NetworkEvents.onConnectToServer();
    }
}
