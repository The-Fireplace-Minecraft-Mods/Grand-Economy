package dev.the_fireplace.grandeconomy.api;

import dev.the_fireplace.grandeconomy.impl.EconomyRegistryImpl;

import java.util.Collection;

public interface EconomyRegistry {
    static EconomyRegistry getInstance() {
        //noinspection deprecation
        return EconomyRegistryImpl.INSTANCE;
    }

    /**
     * Check if an economy handler exists for the given modid or alias
     * @param key
     * A modid or alias
     */
    boolean hasEconomyHandler(String key);

    Economy getEconomyHandler(String key);

    Collection<String> getEconomyHandlers();

    /**
     * Register an economy handler for your mod. This will allow setting the "economy bridge" config option to forModid or one of the aliases to have GE use your economy handler.
     * This must be done BEFORE {@link net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents#SERVER_STARTED}.
     * @param handler
     * Your economy handler instance
     * @param modid
     * The modid the handler is for.
     * @param aliases
     * Aliases that can also be used in the config to use your currency. The method still returns true if any of these are already taken, as long as the modid isn't.
     * @return false if registering the handler failed (currently the only reason is if another handler is registered with the given modid) and true otherwise
     */
    boolean registerEconomyHandler(Economy handler, String modid, String... aliases);
}
