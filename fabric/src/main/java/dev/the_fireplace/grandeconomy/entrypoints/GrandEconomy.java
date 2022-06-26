package dev.the_fireplace.grandeconomy.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import dev.the_fireplace.grandeconomy.gecoin.GECoinEconomyAdapter;

public final class GrandEconomy implements GrandEconomyEntrypoint
{
	@Override
	public void init(EconomyAdapterRegistry economyAdapterRegistry) {
		Injector injector = GrandEconomyConstants.getInjector();
		economyAdapterRegistry.registerEconomyHandler(injector.getInstance(GECoinEconomyAdapter.class), GrandEconomyConstants.MODID);
	}
}
