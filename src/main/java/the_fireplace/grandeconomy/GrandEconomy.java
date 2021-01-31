package the_fireplace.grandeconomy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.api.Economy;
import the_fireplace.grandeconomy.api.EconomyRegistry;
import the_fireplace.grandeconomy.command.RegisterGeCommands;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.nativeeconomy.GrandEconomyEconomy;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.chat.TranslatorManager;
import the_fireplace.lib.api.command.FeedbackSender;
import the_fireplace.lib.api.command.FeedbackSenderManager;

public class GrandEconomy implements ModInitializer {
    public static final String MODID = "grandeconomy";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ModConfig config;

    private static final BoundedEconomyWrapper ECONOMY_WRAPPER = new BoundedEconomyWrapper();
    public static Economy getEconomy() {
        return ECONOMY_WRAPPER;
    }

    private static final TranslatorManager translatorManager = TranslatorManager.getInstance();
    private static Translator translator = null;
    public static Translator getTranslator() {
        if (translator == null) {
            translator = translatorManager.getTranslator(MODID);
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
        translatorManager.addTranslator(MODID);

        ServerLifecycleEvents.SERVER_STARTING.register(s -> {
            loadEconomy();
            RegisterGeCommands.register(s.getCommandManager().getDispatcher());
        });
    }

    static void loadEconomy() {
        Economy economy;
        if (EconomyRegistry.getInstance().hasEconomyHandler(GrandEconomy.config.economyBridge)) {
            economy = EconomyRegistry.getInstance().getEconomyHandler(GrandEconomy.config.economyBridge);
        } else {
            economy = new GrandEconomyEconomy();
            EconomyRegistry.getInstance().registerEconomyHandler(economy, MODID);
        }
        economy.init();
        ECONOMY_WRAPPER.setEconomy(economy);
    }
}
