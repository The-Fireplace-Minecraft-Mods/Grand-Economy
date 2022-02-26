package dev.the_fireplace.grandeconomy.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import dev.the_fireplace.grandeconomy.domain.init.LoaderSpecificInitialization;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

@Implementation
public final class FabricSpecificInitialization implements LoaderSpecificInitialization
{
    @Override
    public void runModAdapterRegistration(EconomyAdapterRegistry economyAdapterRegistry) {
        FabricLoader.getInstance().getEntrypointContainers("grandeconomy", GrandEconomyEntrypoint.class).forEach((entrypoint) -> {
            GrandEconomyEntrypoint api = entrypoint.getEntrypoint();
            api.init(economyAdapterRegistry);
        });
    }

    @Override
    public void registerServerStartingCallback(Consumer<MinecraftServer> runnable) {
        ServerLifecycleEvents.SERVER_STARTING.register(runnable::accept);
    }
}
