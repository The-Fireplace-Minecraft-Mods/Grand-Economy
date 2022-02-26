package dev.the_fireplace.grandeconomy.api.events;

import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event which is called when the economy is selected and ready to use.
 * Subscribe to this event on {@link dev.the_fireplace.lib.api.events.FLEventBus#BUS}.
 */
public final class EconomySelectedEvent extends Event
{
    private final Economy economy;

    public EconomySelectedEvent(Economy economy) {
        this.economy = economy;
    }

    public Economy getEconomy() {
        return economy;
    }
}
