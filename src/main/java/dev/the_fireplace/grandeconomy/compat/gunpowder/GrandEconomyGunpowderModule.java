package dev.the_fireplace.grandeconomy.compat.gunpowder;

import com.google.common.collect.Sets;
import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.Economy;
import dev.the_fireplace.grandeconomy.api.EconomyRegistry;
import dev.the_fireplace.grandeconomy.config.ModConfig;
import io.github.gunpowder.api.GunpowderMod;
import io.github.gunpowder.api.GunpowderModule;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

public class GrandEconomyGunpowderModule implements GunpowderModule {
    @NotNull
    @Override
    public String getName() {
        return GrandEconomy.MODID;
    }

    @Override
    public boolean getToggleable() {
        return false;
    }

    private static final Set<String> GUNPOWDER_NAMES = Sets.newHashSet("gunpowder-api", "gunpowder", "gunpowder-currency");

    @Override
    public void onInitialize() {
        if (GUNPOWDER_NAMES.contains(ModConfig.getData().getEconomyHandler().toLowerCase(Locale.ROOT))) {
            Economy gunpowderEconomy = new GunpowderEconomy();
            EconomyRegistry.getInstance().registerEconomyHandler(gunpowderEconomy, "gunpowder-api", "gunpowder", "gunpowder-currency");
        } else {
            //noinspection LawOfDemeter
            GunpowderMod.getInstance().getRegistry().registerModelHandler(BalanceHandler.class, new GrandEconomyGunpowderBalanceHandler.Supplier());
        }
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfigs() {

    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void reload() {

    }
}
