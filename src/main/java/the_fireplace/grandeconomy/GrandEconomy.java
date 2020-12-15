package the_fireplace.grandeconomy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.EconomyHandler;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.events.NetworkEvents;
import the_fireplace.grandeconomy.nativeeconomy.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.requesthandler.GrandEconomyApiImpl;

import java.util.UUID;

public class GrandEconomy implements ModInitializer {
    public static final String MODID = "grandeconomy";
    private static MinecraftServer minecraftServer;
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ModConfig config;
    private static EconomyHandler economy;
    private static final EconomyHandler economyWrapper = new EconomyHandler() {
        @Override
        public double getBalance(UUID uuid, Boolean isPlayer) {
            return economy.getBalance(uuid, isPlayer);
        }

        @Override
        public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
            if(GrandEconomy.config.enforceNonNegativeBalance && amount < 0) {
                if(getBalance(uuid, isPlayer)+amount < 0)
                    return false;
            }
            return economy.addToBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
            if(GrandEconomy.config.enforceNonNegativeBalance && amount > 0) {
                if(getBalance(uuid, isPlayer)-amount < 0)
                    return false;
            }
            return economy.takeFromBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
            if(GrandEconomy.config.enforceNonNegativeBalance && amount < 0)
                return false;
            return economy.setBalance(uuid, amount, isPlayer);
        }

        @Override
        public String getCurrencyName(double amount) {
            return economy.getCurrencyName(amount);
        }

        @Override
        public String getFormattedCurrency(double amount) {
            return economy.getFormattedCurrency(amount);
        }

        @Override
        public String getId() {
            return economy.getId();
        }

        @Override
        public void init() {
            economy.init();
        }
    };
    public static EconomyHandler getEconomy() {
        return economyWrapper;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    @Override
    public void onInitialize() {
        config = ModConfig.load();
        config.save();

        new GrandEconomyApiImpl();
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            minecraftServer = s;
            loadEconomy();
            GeCommands.register(s.getCommandManager().getDispatcher());
        });
        NetworkEvents.init();
    }

    static void loadEconomy() {
        economy = GrandEconomyApi.getEconomyHandlers().getOrDefault(GrandEconomy.config.economyBridge, new GrandEconomyEconHandler());
        if(economy.getClass().equals(GrandEconomyEconHandler.class))
            GrandEconomyApi.registerEconomyHandler(economy, MODID);
        economy.init();
    }
}
