package dev.the_fireplace.grandeconomy.compat.gunpowder;

import com.google.common.collect.Sets;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.interfaces.Economy;
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
    private final EconomyRegistry economyRegistry;

    @Inject
    public GrandEconomyGunpowderModule(ConfigValues configValues, EconomyRegistry economyRegistry) {
        this.configValues = configValues;
        this.economyRegistry = economyRegistry;
    }

    public void onInitialize() {
        if (GUNPOWDER_NAMES.contains(configValues.getEconomyHandler().toLowerCase(Locale.ROOT))) {
            Economy gunpowderEconomy = new GunpowderEconomy();
            economyRegistry.registerEconomyHandler(gunpowderEconomy, "gunpowder-api", "gunpowder", "gunpowder-currency");
        } else {
            //noinspection LawOfDemeter
            GunpowderMod.getInstance().getRegistry().registerModelHandler(BalanceHandler.class, new GrandEconomyGunpowderBalanceHandler.Supplier());
        }
    }
}
