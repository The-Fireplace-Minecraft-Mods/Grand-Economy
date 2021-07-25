package dev.the_fireplace.grandeconomy.entrypoints;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.compat.gunpowder.GrandEconomyGunpowderModule;
import io.github.gunpowder.api.GunpowderModule;
import org.jetbrains.annotations.NotNull;

public final class GunpowderEntrypoint implements GunpowderModule {
    @Override
    @NotNull
    public String getName() {
        return GrandEconomyConstants.MODID;
    }

    @Override
    public boolean getToggleable() {
        return false;
    }

    @Override
    public void onInitialize() {
        DIContainer.get().getInstance(GrandEconomyGunpowderModule.class).onInitialize();
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
