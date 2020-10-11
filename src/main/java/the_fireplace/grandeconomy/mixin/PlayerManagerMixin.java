package the_fireplace.grandeconomy.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.grandeconomy.events.NetworkEvents;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("HEAD"), method = "remove")
    public void remove(ServerPlayerEntity player, CallbackInfo info) {
        NetworkEvents.onDisconnected(player.getUuid());
    }
}
