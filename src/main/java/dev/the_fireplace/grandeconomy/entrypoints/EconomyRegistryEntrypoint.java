package dev.the_fireplace.grandeconomy.entrypoints;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.gecoin.GECoinEconomy;

public final class EconomyRegistryEntrypoint implements GrandEconomyEntrypoint {
	@Override
	public void init(EconomyRegistry economyRegistry) {
		economyRegistry.registerEconomyHandler(DIContainer.get().getInstance(GECoinEconomy.class), GrandEconomyConstants.MODID);
	}
}
