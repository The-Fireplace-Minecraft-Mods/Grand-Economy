package dev.the_fireplace.grandeconomy.eventtrigger;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.EconomySelectedEvent;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.EconomySelectedTrigger;

@Implementation
public final class FabricEconomySelectedTrigger implements EconomySelectedTrigger
{
    @Override
    public void triggerSelected(Economy economy) {
        EconomySelectedEvent.EVENT.invoker().onEconomySelected(economy);
    }
}
