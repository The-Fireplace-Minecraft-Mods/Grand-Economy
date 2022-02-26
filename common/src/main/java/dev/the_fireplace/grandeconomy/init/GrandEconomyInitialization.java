package dev.the_fireplace.grandeconomy.init;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.command.RegisterGeCommands;
import dev.the_fireplace.grandeconomy.domain.init.LoaderSpecificInitialization;
import dev.the_fireplace.grandeconomy.economy.EconomyAdapterRegistryImpl;
import dev.the_fireplace.grandeconomy.economy.EconomyImplementationLoader;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;

import javax.inject.Inject;

public final class GrandEconomyInitialization
{
    private final LoaderSpecificInitialization loaderSpecificInitialization;
    private final EconomyAdapterRegistryImpl economyAdapterRegistry;
    private final TranslatorFactory translatorFactory;
    private final EconomyImplementationLoader economyImplementationLoader;
    private final RegisterGeCommands registerGeCommands;

    @Inject
    public GrandEconomyInitialization(
        LoaderSpecificInitialization loaderSpecificInitialization,
        EconomyAdapterRegistryImpl economyAdapterRegistry,
        TranslatorFactory translatorFactory,
        EconomyImplementationLoader economyImplementationLoader,
        RegisterGeCommands registerGeCommands
    ) {
        this.loaderSpecificInitialization = loaderSpecificInitialization;
        this.economyAdapterRegistry = economyAdapterRegistry;
        this.translatorFactory = translatorFactory;
        this.economyImplementationLoader = economyImplementationLoader;
        this.registerGeCommands = registerGeCommands;
    }

    public void init() {
        loaderSpecificInitialization.runModAdapterRegistration(economyAdapterRegistry);
        translatorFactory.addTranslator(GrandEconomyConstants.MODID);
        economyImplementationLoader.initialLoad();

        loaderSpecificInitialization.registerServerStartingCallback(s -> {
            registerGeCommands.register(s.getCommands().getDispatcher());
        });
    }
}
