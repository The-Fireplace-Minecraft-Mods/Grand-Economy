package dev.the_fireplace.grandeconomy.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.adapters.DiamondEconomyAdapter;
import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import dev.the_fireplace.grandeconomy.gecoin.GECoinEconomyAdapter;
import net.fabricmc.loader.api.FabricLoader;

public final class GrandEconomy implements GrandEconomyEntrypoint
{
	public static final String DIAMOND_ECONOMY_MODID = "diamondeconomy";

	@Override
	public void init(EconomyAdapterRegistry economyAdapterRegistry) {
		Injector injector = GrandEconomyConstants.getInjector();
		economyAdapterRegistry.registerEconomyHandler(injector.getInstance(GECoinEconomyAdapter.class), GrandEconomyConstants.MODID);
		EconomyAdapter adapter;
		if (FabricLoader.getInstance().isModLoaded(DIAMOND_ECONOMY_MODID)) {
			adapter = injector.getInstance(DiamondEconomyAdapter.class);
			economyAdapterRegistry.registerEconomyHandler(adapter, DIAMOND_ECONOMY_MODID);
		}
	}
}
