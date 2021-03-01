package dev.the_fireplace.grandeconomy;

import dev.the_fireplace.grandeconomy.api.Economy;
import dev.the_fireplace.grandeconomy.api.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.command.RegisterGeCommands;
import dev.the_fireplace.grandeconomy.config.ModConfig;
import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.api.chat.TranslatorManager;
import dev.the_fireplace.lib.api.command.FeedbackSender;
import dev.the_fireplace.lib.api.command.FeedbackSenderManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GrandEconomy implements ModInitializer {
    public static final String MODID = "grandeconomy";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Logger getLogger() {
        return LOGGER;
    }
    private static final BoundedEconomyWrapper ECONOMY_WRAPPER = new BoundedEconomyWrapper();
    public static Economy getEconomy() {
        return ECONOMY_WRAPPER;
    }

    private static final TranslatorManager TRANSLATOR_MANAGER = TranslatorManager.getInstance();
    private static final Translator TRANSLATOR = TRANSLATOR_MANAGER.getTranslator(MODID);
    public static Translator getTranslator() {
        return TRANSLATOR;
    }
    private static final FeedbackSender FEEDBACK_SENDER = FeedbackSenderManager.getInstance().get(TRANSLATOR);
    public static FeedbackSender getFeedbackSender() {
        return FEEDBACK_SENDER;
    }

    private static final EconomyRegistry ECONOMY_REGISTRY = EconomyRegistry.getInstance();

    @Override
    public void onInitialize() {
        TRANSLATOR_MANAGER.addTranslator(MODID);
        FabricLoader.getInstance().getEntrypointContainers("grandeconomy", GrandEconomyEntrypoint.class).forEach((entrypoint) -> {
            GrandEconomyEntrypoint api = entrypoint.getEntrypoint();
            api.init(ECONOMY_REGISTRY);
        });
        loadEconomy();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            RegisterGeCommands.register(s.getCommandManager().getDispatcher());
        });
    }

    private static void loadEconomy() {
        String bridge = ModConfig.getData().getEconomyHandler();
        if (!ECONOMY_REGISTRY.hasEconomyHandler(bridge)) {
            LOGGER.warn("Economy '{}' not found, defaulting to Grand Economy's native economy.", bridge);
            bridge = MODID;
        }
        Economy economy = ECONOMY_REGISTRY.getEconomyHandler(bridge);
        economy.init();
        ECONOMY_WRAPPER.setEconomy(economy);
    }
}
