package dev.the_fireplace.grandeconomy.domain.init;

import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public interface LoaderSpecificInitialization
{
    void runModAdapterRegistration(EconomyAdapterRegistry economyAdapterRegistry);

    void registerServerStartingCallback(Consumer<MinecraftServer> runnable);
}
