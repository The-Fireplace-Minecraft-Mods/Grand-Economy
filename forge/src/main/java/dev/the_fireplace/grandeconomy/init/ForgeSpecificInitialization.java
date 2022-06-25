package dev.the_fireplace.grandeconomy.init;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.EconomyRegistryEvent;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import dev.the_fireplace.grandeconomy.domain.init.LoaderSpecificInitialization;
import dev.the_fireplace.lib.api.events.FLEventBus;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;

import java.util.function.Consumer;

@Implementation
public final class ForgeSpecificInitialization implements LoaderSpecificInitialization
{
    @Override
    public void runModAdapterRegistration(EconomyAdapterRegistry economyAdapterRegistry) {
        FLEventBus.BUS.post(new EconomyRegistryEvent(economyAdapterRegistry));
    }

    @Override
    public void registerServerStartingCallback(Consumer<MinecraftServer> runnable) {
        MinecraftForge.EVENT_BUS.register(new ServerStarting(runnable));
    }

    private record ServerStarting(Consumer<MinecraftServer> runnable)
    {
        @SubscribeEvent
        public void onServerStarting(FMLServerStartingEvent event) {
            runnable.accept(event.getServer());
        }
    }
}
