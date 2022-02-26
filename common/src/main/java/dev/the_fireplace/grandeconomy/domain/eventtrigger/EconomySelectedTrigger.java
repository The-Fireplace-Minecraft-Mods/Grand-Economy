package dev.the_fireplace.grandeconomy.domain.eventtrigger;

import dev.the_fireplace.grandeconomy.api.injectables.Economy;

public interface EconomySelectedTrigger
{
    void triggerSelected(Economy economy);
}
