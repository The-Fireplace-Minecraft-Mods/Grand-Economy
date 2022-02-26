package dev.the_fireplace.grandeconomy.entrypoints;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.config.GEConfigScreenFactory;
import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;

public final class ConfigGui implements ConfigGuiEntrypoint
{
    @Override
    public void registerConfigGuis(ConfigGuiRegistry configGuiRegistry) {
        GEConfigScreenFactory configScreenFactory = GrandEconomyConstants.getInjector().getInstance(GEConfigScreenFactory.class);
        configGuiRegistry.register(GrandEconomyConstants.MODID, configScreenFactory::getConfigScreen);
    }
}
