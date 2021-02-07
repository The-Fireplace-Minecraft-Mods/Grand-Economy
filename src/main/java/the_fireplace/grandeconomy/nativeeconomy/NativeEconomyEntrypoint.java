package the_fireplace.grandeconomy.nativeeconomy;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.EconomyRegistry;
import the_fireplace.grandeconomy.api.GrandEconomyEntrypoint;

public class NativeEconomyEntrypoint implements GrandEconomyEntrypoint {
	@Override
	public void init(EconomyRegistry economyRegistry) {
		economyRegistry.registerEconomyHandler(new NativeEconomy(), GrandEconomy.MODID);
	}
}
