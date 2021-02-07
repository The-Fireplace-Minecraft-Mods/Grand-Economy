package the_fireplace.grandeconomy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.Economy;
import the_fireplace.grandeconomy.api.EconomyRegistry;
import the_fireplace.grandeconomy.api.GrandEconomyEntrypoint;
import the_fireplace.grandeconomy.command.RegisterGeCommands;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.chat.TranslatorManager;
import the_fireplace.lib.api.command.FeedbackSender;
import the_fireplace.lib.api.command.FeedbackSenderManager;

public class GrandEconomy implements ModInitializer {
    public static final String MODID = "grandeconomy";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Logger getLogger() {
        return LOGGER;
    }
    private static ModConfig config;
    public static ModConfig getConfig() {
        return config;
    }
    private static final BoundedEconomyWrapper ECONOMY_WRAPPER = new BoundedEconomyWrapper();
    public static Economy getEconomy() {
        return ECONOMY_WRAPPER;
    }

    private static final TranslatorManager TRANSLATOR_MANAGER = TranslatorManager.getInstance();
    private static Translator translator = null;
    public static Translator getTranslator() {
        if (translator == null) {
            translator = TRANSLATOR_MANAGER.getTranslator(MODID);
        }
        return translator;
    }
    private static FeedbackSender feedbackSender = null;
    public static FeedbackSender getFeedbackSender() {
        if (feedbackSender == null) {
            feedbackSender = FeedbackSenderManager.getInstance().get(getTranslator());
        }
        return feedbackSender;
    }

    @Override
    public void onInitialize() {
        config = ModConfig.load();
        config.save();
        TRANSLATOR_MANAGER.addTranslator(MODID);
        FabricLoader.getInstance().getEntrypointContainers("grandeconomy", GrandEconomyEntrypoint.class).forEach((entrypoint) -> {
            GrandEconomyEntrypoint api = entrypoint.getEntrypoint();
            api.init(EconomyRegistry.getInstance());
        });
        loadEconomy();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            RegisterGeCommands.register(s.getCommandManager().getDispatcher());
        });
    }

    static void loadEconomy() {
        EconomyRegistry economyRegistry = EconomyRegistry.getInstance();
        String bridge = GrandEconomy.config.economyBridge;
        if (!economyRegistry.hasEconomyHandler(bridge)) {
            getLogger().warn("Economy '{}' not found, defaulting to Grand Economy's native economy.", bridge);
            bridge = MODID;
        }
        Economy economy = economyRegistry.getEconomyHandler(bridge);
        economy.init();
        ECONOMY_WRAPPER.setEconomy(economy);
    }
}
