package the_fireplace.grandeconomy.fabric.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(at = @At("RETURN"), method = "onPlayerConnected(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void onPlayerConnected(ServerPlayerEntity player, CallbackInfo info) {
        the_fireplace.grandeconomy.fabric.events.NetworkEvents.onPlayerJoinServer(player);
    }
}
