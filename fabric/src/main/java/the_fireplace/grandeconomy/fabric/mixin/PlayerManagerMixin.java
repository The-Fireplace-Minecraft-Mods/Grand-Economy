package the_fireplace.grandeconomy.fabric.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("HEAD"), method = "remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void remove(ServerPlayerEntity player, CallbackInfo info) {
        the_fireplace.grandeconomy.fabric.events.NetworkEvents.onDisconnected(player.getUuid());
    }
}
