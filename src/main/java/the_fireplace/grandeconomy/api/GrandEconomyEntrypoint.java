package the_fireplace.grandeconomy.api;

public interface GrandEconomyEntrypoint {
	/**
	 * Called during initialization, this provides a chance to register your {@link the_fireplace.grandeconomy.api.Economy} before the economy setting
	 *  has been read from the config, set your mod to use Grand Economy, and/or subscribe to events.
	 */
	void init(EconomyRegistry economyRegistry);
}
