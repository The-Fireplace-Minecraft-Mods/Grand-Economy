package dev.the_fireplace.grandeconomy.mixin;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.eventhandlers.PlayerDiedHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"ConstantConditions", "unused"})
@Mixin(ServerPlayer.class)
public final class ServerPlayerMixin
{
    @Inject(at = @At("HEAD"), method = "die")
    private void die(DamageSource damageSource, CallbackInfo info) {
        GrandEconomyConstants.getInjector().getInstance(PlayerDiedHandler.class).onPlayerDeath((ServerPlayer) (Object) this, damageSource);
    }
}
