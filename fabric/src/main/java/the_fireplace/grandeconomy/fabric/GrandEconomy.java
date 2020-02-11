package the_fireplace.grandeconomy.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.GrandEconomyApiFabric;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.fabric.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.fabric.events.NetworkEvents;

import java.io.File;

public class GrandEconomy implements ModInitializer {
    private static MinecraftServer minecraftServer;
    public static final Logger LOGGER = LogManager.getLogger(GrandEconomyApi.MODID);

    public static File configDir;

    private static IEconHandler economy;
    public static IEconHandler getEconomy() {
        return economy;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    @Override
    public void onInitialize() {
        new GrandEconomyApiFabric();
        ServerStartCallback.EVENT.register(s -> {
            minecraftServer = s;
            economy = GrandEconomyApi.getEconHandlers().getOrDefault(Config.economyBridge, new GrandEconomyEconHandler());
            if(economy.getClass().equals(GrandEconomyEconHandler.class))
                GrandEconomyApi.registerEconomyHandler(economy, GrandEconomyApi.MODID);
            economy.init();
            configDir = new File(new File(s.getRunDirectory(), "config"), "grandeconomy-extra");
            configDir.mkdirs();
            GeCommands.register(s.getCommandManager().getDispatcher());
        });
        NetworkEvents.init();
    }
}
