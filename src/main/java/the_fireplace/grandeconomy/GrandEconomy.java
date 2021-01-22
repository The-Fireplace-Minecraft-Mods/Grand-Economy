package the_fireplace.grandeconomy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.EconomyHandler;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.command.GeCommands;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.nativeeconomy.GrandEconomyEconHandler;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.chat.TranslatorManager;
import the_fireplace.lib.impl.FireplaceLib;

import java.util.UUID;

public class GrandEconomy implements ModInitializer {
    public static final String MODID = "grandeconomy";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ModConfig config;

    private static final BoundedEconomyWrapper ECONOMY_WRAPPER = new BoundedEconomyWrapper();
    public static EconomyHandler getEconomy() {
        return ECONOMY_WRAPPER;
    }

    public static MinecraftServer getServer() {
        return FireplaceLib.getServer();
    }

    private static final TranslatorManager translatorManager = TranslatorManager.getInstance();
    private static Translator translator = null;
    public static Translator getTranslator() {
        if (translator == null) {
            translator = translatorManager.getTranslator(MODID);
        }
        return translator;
    }

    @Override
    public void onInitialize() {
        config = ModConfig.load();
        config.save();
        translatorManager.addTranslator(MODID);

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            loadEconomy();
            GeCommands.register(s.getCommandManager().getDispatcher());
        });
    }

    static void loadEconomy() {
        EconomyHandler economy;
        if (GrandEconomyApi.hasEconomyHandler(GrandEconomy.config.economyBridge)) {
            economy = GrandEconomyApi.getEconomyHandler(GrandEconomy.config.economyBridge);
        } else {
            economy = new GrandEconomyEconHandler();
            GrandEconomyApi.registerEconomyHandler(economy, MODID);
        }
        economy.init();
        ECONOMY_WRAPPER.setEconomy(economy);
    }

    private static class BoundedEconomyWrapper implements EconomyHandler {
        EconomyHandler economy;

        private void setEconomy(EconomyHandler economy) {
            this.economy = economy;
        }

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
    }
}
