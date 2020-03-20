package the_fireplace.grandeconomy.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.GrandEconomyApiFabric;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.fabric.earnings.ConversionItems;
import the_fireplace.grandeconomy.fabric.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.fabric.events.NetworkEvents;

import java.io.File;
import java.util.UUID;

public class GrandEconomy implements ModInitializer {
    private static MinecraftServer minecraftServer;
    public static final Logger LOGGER = LogManager.getLogger(GrandEconomyApi.MODID);

    public static File configDir;

    private static IEconHandler economy;
    private static IEconHandler economyWrapper = new IEconHandler() {
        @Override
        public long getBalance(UUID uuid, Boolean isPlayer) {
            return economy.getBalance(uuid, isPlayer);
        }

        @Override
        public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
            if(Config.enforceNonNegativeBalance && amount < 0) {
                if(getBalance(uuid, isPlayer)+amount < 0)
                    return false;
            }
            return economy.addToBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
            if(Config.enforceNonNegativeBalance && amount > 0) {
                if(getBalance(uuid, isPlayer)-amount < 0)
                    return false;
            }
            return economy.takeFromBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
            if(Config.enforceNonNegativeBalance && amount < 0)
                return false;
            return economy.setBalance(uuid, amount, isPlayer);
        }

        @Override
        public String getCurrencyName(long amount) {
            return economy.getCurrencyName(amount);
        }

        @Override
        public String getFormattedCurrency(long amount) {
            return economy.getFormattedCurrency(amount);
        }

        @Override
        public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
            return economy.ensureAccountExists(uuid, isPlayer);
        }

        @Override
        public Boolean forceSave(UUID uuid, Boolean isPlayer) {
            return economy.forceSave(uuid, isPlayer);
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
    public static IEconHandler getEconomy() {
        return economyWrapper;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    @Override
    public void onInitialize() {
        new GrandEconomyApiFabric();
        ServerStartCallback.EVENT.register(s -> {
            minecraftServer = s;
            Config.load();
            economy = GrandEconomyApi.getEconHandlers().getOrDefault(Config.economyBridge, new GrandEconomyEconHandler());
            if(economy.getClass().equals(GrandEconomyEconHandler.class))
                GrandEconomyApi.registerEconomyHandler(economy, GrandEconomyApi.MODID);
            economy.init();
            configDir = new File(new File(s.getRunDirectory(), "config"), "grandeconomy-extra");
            configDir.mkdirs();
            //Initialize ConversionItems
            ConversionItems.hasValue(null);
            GeCommands.register(s.getCommandManager().getDispatcher());
        });
        NetworkEvents.init();
    }
}
