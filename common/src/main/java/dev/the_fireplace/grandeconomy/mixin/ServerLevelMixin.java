package dev.the_fireplace.grandeconomy.mixin;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.eventhandlers.PlayerJoinedServerHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ServerLevel.class)
public final class ServerLevelMixin
{
    @Inject(at = @At("RETURN"), method = "addNewPlayer")
    private void onPlayerConnected(ServerPlayer player, CallbackInfo info) {
        GrandEconomyConstants.getInjector().getInstance(PlayerJoinedServerHandler.class).onPlayerJoinServer(player);
    }
}
