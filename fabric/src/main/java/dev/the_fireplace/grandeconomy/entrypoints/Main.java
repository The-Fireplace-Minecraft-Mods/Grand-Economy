package dev.the_fireplace.grandeconomy.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.command.RegisterGeCommands;
import dev.the_fireplace.grandeconomy.economy.EconomyAdapterRegistryImpl;
import dev.the_fireplace.grandeconomy.economy.EconomyImplementationLoader;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class Main implements ModInitializer
{
    @Override
    public void onInitialize() {
        Injector injector = GrandEconomyConstants.getInjector();
        EconomyAdapterRegistryImpl economyAdapterRegistry = injector.getInstance(EconomyAdapterRegistryImpl.class);
        FabricLoader.getInstance().getEntrypointContainers("grandeconomy", GrandEconomyEntrypoint.class).forEach((entrypoint) -> {
            GrandEconomyEntrypoint api = entrypoint.getEntrypoint();
            api.init(economyAdapterRegistry);
        });
        injector.getInstance(TranslatorFactory.class).addTranslator(GrandEconomyConstants.MODID);
        injector.getInstance(EconomyImplementationLoader.class).initialLoad();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            injector.getInstance(RegisterGeCommands.class).register(s.getCommands().getDispatcher());
        });
    }
}
