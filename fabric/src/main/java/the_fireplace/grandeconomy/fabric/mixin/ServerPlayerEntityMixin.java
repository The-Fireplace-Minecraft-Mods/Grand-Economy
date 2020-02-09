package the_fireplace.grandeconomy.fabric.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        the_fireplace.grandeconomy.fabric.events.KillingEvents.onPlayerDeath((ServerPlayerEntity)(Object)this, damageSource);
    }
}
