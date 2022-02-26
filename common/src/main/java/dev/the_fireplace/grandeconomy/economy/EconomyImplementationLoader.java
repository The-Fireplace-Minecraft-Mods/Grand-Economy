package dev.the_fireplace.grandeconomy.economy;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;

import javax.inject.Inject;

public final class EconomyImplementationLoader
{
    private final EconomyImplementationWrapper economyImplementationWrapper;
    private final ConfigValues configValues;
    private final EconomyAdapterRegistryImpl economyAdapterRegistry;

    @Inject
    public EconomyImplementationLoader(EconomyImplementationWrapper economyImplementationWrapper, ConfigValues configValues, EconomyAdapterRegistryImpl economyAdapterRegistry) {
        this.economyImplementationWrapper = economyImplementationWrapper;
        this.configValues = configValues;
        this.economyAdapterRegistry = economyAdapterRegistry;
    }

    public void initialLoad() {
        loadAdapter(configValues.getEconomyHandler());
    }

    private void loadAdapter(String adapterId) {
        if (!economyAdapterRegistry.hasEconomyHandler(adapterId)) {
            GrandEconomyConstants.getLogger().warn("Economy '{}' not found, defaulting to Grand Economy's GECoin currency.", adapterId);
            adapterId = GrandEconomyConstants.MODID;
        }
        EconomyAdapter economyAdapter = economyAdapterRegistry.getEconomyHandler(adapterId);
        economyAdapter.init();
        economyImplementationWrapper.setEconomy(economyAdapter);
    }
}
