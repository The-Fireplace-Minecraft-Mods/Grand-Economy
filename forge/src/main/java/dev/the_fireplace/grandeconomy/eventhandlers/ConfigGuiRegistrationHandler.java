package dev.the_fireplace.grandeconomy.eventhandlers;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.config.GEConfigScreenFactory;
import dev.the_fireplace.lib.api.events.ConfigScreenRegistration;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.inject.Inject;

public final class ConfigGuiRegistrationHandler
{
    private final GEConfigScreenFactory configScreenFactory;

    @Inject
    public ConfigGuiRegistrationHandler(GEConfigScreenFactory configScreenFactory) {
        this.configScreenFactory = configScreenFactory;
    }

    @SubscribeEvent
    public void registerConfigGui(ConfigScreenRegistration configScreenRegistration) {
        configScreenRegistration.getConfigGuiRegistry().register(GrandEconomyConstants.MODID, configScreenFactory::getConfigScreen);
    }
}
