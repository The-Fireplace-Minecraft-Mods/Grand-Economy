package the_fireplace.grandeconomy.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.grandeconomy.events.KillingEvents;

@SuppressWarnings({"ConstantConditions", "unused"})
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        KillingEvents.onPlayerDeath((ServerPlayerEntity)(Object)this, damageSource);
    }
}
