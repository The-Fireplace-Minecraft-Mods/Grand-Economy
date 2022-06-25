package dev.the_fireplace.grandeconomy.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.eventhandlers.ConfigGuiRegistrationHandler;
import dev.the_fireplace.grandeconomy.eventhandlers.EconomyRegistrationHandler;
import dev.the_fireplace.grandeconomy.init.GrandEconomyInitialization;
import dev.the_fireplace.lib.api.events.FLEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod("grandeconomy")
public final class Forge
{
    public Forge() {
        Injector injector = GrandEconomyConstants.getInjector();
        FLEventBus.BUS.register(injector.getInstance(EconomyRegistrationHandler.class));
        injector.getInstance(GrandEconomyInitialization.class).init();
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            FLEventBus.BUS.register(injector.getInstance(ConfigGuiRegistrationHandler.class));
            return null;
        });

        // Register as optional on both sides
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
