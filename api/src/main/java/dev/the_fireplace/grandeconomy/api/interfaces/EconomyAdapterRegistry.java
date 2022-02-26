package dev.the_fireplace.grandeconomy.api.interfaces;

@SuppressWarnings("UnusedReturnValue")
public interface EconomyAdapterRegistry
{
    /**
     * Register an economy handler for your mod. This will allow setting the "economy bridge" config option to your modid or one of the aliases to have GE use your economy handler.
     *
     * @param handler Your economy handler instance
     * @param modid   The modid the handler is for.
     * @param aliases Aliases that can also be used in the config to use your currency. The method still returns true if any of these are already taken, as long as the modid isn't.
     * @return false if registering the handler failed (currently the only reason is if another handler is registered with the given modid) and true otherwise
     */
    boolean registerEconomyHandler(EconomyAdapter handler, String modid, String... aliases);
}
