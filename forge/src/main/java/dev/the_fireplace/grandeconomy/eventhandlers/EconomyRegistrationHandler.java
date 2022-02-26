package dev.the_fireplace.grandeconomy.eventhandlers;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.events.EconomyRegistryEvent;
import dev.the_fireplace.grandeconomy.gecoin.GECoinEconomyAdapter;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.inject.Inject;

public final class EconomyRegistrationHandler
{
    private final GECoinEconomyAdapter gecoinAdapter;

    @Inject
    public EconomyRegistrationHandler(GECoinEconomyAdapter gecoinAdapter) {
        this.gecoinAdapter = gecoinAdapter;
    }

    @SubscribeEvent
    public void onEconomyRegistration(EconomyRegistryEvent event) {
        event.getRegistry().registerEconomyHandler(gecoinAdapter, GrandEconomyConstants.MODID);
        //TODO custom mod adapters here
    }
}
