package dev.the_fireplace.grandeconomy.mixin;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.events.NetworkEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ServerWorld.class)
public final class ServerWorldMixin {
    @Inject(at = @At("RETURN"), method = "onPlayerConnected")
    private void onPlayerConnected(ServerPlayerEntity player, CallbackInfo info) {
        DIContainer.get().getInstance(NetworkEvents.class).onPlayerJoinServer(player);
    }
}
