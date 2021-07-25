package dev.the_fireplace.grandeconomy.api.entrypoints;

import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.interfaces.Economy;

public interface GrandEconomyEntrypoint {
	/**
	 * Called during initialization, this provides a chance to register your {@link Economy} before the economy setting
	 *  has been read from the config, set your mod to use Grand Economy, and/or subscribe to events.
	 */
	void init(EconomyRegistry economyRegistry);
}
