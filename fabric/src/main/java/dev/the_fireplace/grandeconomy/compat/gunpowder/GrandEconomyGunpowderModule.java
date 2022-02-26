package dev.the_fireplace.grandeconomy.compat.gunpowder;

import com.google.common.collect.Sets;
import dev.the_fireplace.grandeconomy.adapters.GunpowderAdapter;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import io.github.gunpowder.api.GunpowderMod;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Locale;
import java.util.Set;

@Singleton
public final class GrandEconomyGunpowderModule {

    private static final Set<String> GUNPOWDER_NAMES = Sets.newHashSet("gunpowder-api", "gunpowder", "gunpowder-currency");

    private final ConfigValues configValues;
    private final EconomyAdapterRegistry economyRegistry;

    @Inject
    public GrandEconomyGunpowderModule(ConfigValues configValues, EconomyAdapterRegistry economyRegistry) {
        this.configValues = configValues;
        this.economyRegistry = economyRegistry;
    }

    public void onInitialize() {
        if (GUNPOWDER_NAMES.contains(configValues.getEconomyHandler().toLowerCase(Locale.ROOT))) {
            EconomyAdapter gunpowderEconomy = new GunpowderAdapter();
            economyRegistry.registerEconomyHandler(gunpowderEconomy, "gunpowder-api", "gunpowder", "gunpowder-currency");
        } else {
            GunpowderMod.getInstance().getRegistry().registerModelHandler(BalanceHandler.class, new GrandEconomyGunpowderBalanceHandler.Supplier());
        }
    }
}
