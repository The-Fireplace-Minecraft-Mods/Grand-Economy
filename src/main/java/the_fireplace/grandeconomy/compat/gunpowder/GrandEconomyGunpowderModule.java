package the_fireplace.grandeconomy.compat.gunpowder;

import com.google.common.collect.Sets;
import io.github.gunpowder.api.GunpowderMod;
import io.github.gunpowder.api.GunpowderModule;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;
import org.jetbrains.annotations.NotNull;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.EconomyHandler;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

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
        if (GUNPOWDER_NAMES.contains(GrandEconomy.config.economyBridge.toLowerCase(Locale.ROOT))) {
            EconomyHandler gunpowderEconomyHandler = new GunpowderEconomyHandler();
            GrandEconomyApi.registerEconomyHandler(gunpowderEconomyHandler, "gunpowder-api", "gunpowder", "gunpowder-currency");
        } else {
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
