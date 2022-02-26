package dev.the_fireplace.grandeconomy.api.events;

import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event which is called when it's time to register economy adapters.
 * Subscribe to this event on {@link dev.the_fireplace.lib.api.events.FLEventBus#BUS}.
 */
public final class EconomyRegistryEvent extends Event
{
    private final EconomyAdapterRegistry registry;

    public EconomyRegistryEvent(EconomyAdapterRegistry registry) {
        this.registry = registry;
    }

    public EconomyAdapterRegistry getRegistry() {
        return registry;
    }
}
