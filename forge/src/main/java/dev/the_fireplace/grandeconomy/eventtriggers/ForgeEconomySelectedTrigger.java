package dev.the_fireplace.grandeconomy.eventtriggers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.EconomySelectedEvent;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.EconomySelectedTrigger;
import dev.the_fireplace.lib.api.events.FLEventBus;

@Implementation
public final class ForgeEconomySelectedTrigger implements EconomySelectedTrigger
{
    @Override
    public void triggerSelected(Economy economy) {
        FLEventBus.BUS.post(new EconomySelectedEvent(economy));
    }
}
