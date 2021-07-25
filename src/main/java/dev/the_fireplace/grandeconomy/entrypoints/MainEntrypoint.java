package dev.the_fireplace.grandeconomy.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.entrypoints.DIModInitializer;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.interfaces.Economy;
import dev.the_fireplace.grandeconomy.command.RegisterGeCommands;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.impl.CurrencyManager;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class MainEntrypoint implements DIModInitializer {
    private CurrencyManager currencyManager;
    private EconomyRegistry economyRegistry;

    @Override
    public void onInitialize(Injector diContainer) {
        currencyManager = diContainer.getInstance(CurrencyManager.class);
        economyRegistry = diContainer.getInstance(EconomyRegistry.class);

        diContainer.getInstance(TranslatorFactory.class).addTranslator(GrandEconomyConstants.MODID);
        FabricLoader.getInstance().getEntrypointContainers("grandeconomy", GrandEconomyEntrypoint.class).forEach((entrypoint) -> {
            GrandEconomyEntrypoint api = entrypoint.getEntrypoint();
            api.init(economyRegistry);
        });
        loadEconomy(diContainer.getInstance(ConfigValues.class).getEconomyHandler());

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            diContainer.getInstance(RegisterGeCommands.class).register(s.getCommandManager().getDispatcher());
        });
    }

    private void loadEconomy(String bridge) {
        if (!economyRegistry.hasEconomyHandler(bridge)) {
            GrandEconomyConstants.getLogger().warn("Economy '{}' not found, defaulting to Grand Economy's GECoin currency.", bridge);
            bridge = GrandEconomyConstants.MODID;
        }
        Economy economy = economyRegistry.getEconomyHandler(bridge);
        economy.init();
        currencyManager.setEconomy(economy);
    }
}
