package the_fireplace.grandeconomy.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.grandeconomy.events.NetworkEvents;

@Mixin(ClientPlayNetworkHandler.class)
@Environment(EnvType.CLIENT)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("RETURN"), method = "onGameJoin")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        NetworkEvents.onConnectToServer();
    }
}
