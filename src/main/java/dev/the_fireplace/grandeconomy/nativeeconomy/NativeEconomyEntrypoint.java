package dev.the_fireplace.grandeconomy.nativeeconomy;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.GrandEconomyEntrypoint;

public class NativeEconomyEntrypoint implements GrandEconomyEntrypoint {
	@Override
	public void init(EconomyRegistry economyRegistry) {
		economyRegistry.registerEconomyHandler(new NativeEconomy(), GrandEconomy.MODID);
	}
}
