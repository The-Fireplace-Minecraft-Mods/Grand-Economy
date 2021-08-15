package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Singleton
public final class GEConfigScreenFactory {
    private static final String TRANSLATION_BASE = "text.config." + GrandEconomyConstants.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = TRANSLATION_BASE + "option.";

    private final Translator translator;
    private final ConfigStateManager configStateManager;
    private final GEConfig config;
    private final ConfigValues defaultConfigValues;
    private final ConfigScreenBuilderFactory configScreenBuilderFactory;
    private final EconomyRegistry economyRegistry;

    private ConfigScreenBuilder configScreenBuilder;

    @Inject
    public GEConfigScreenFactory(
        TranslatorFactory translatorFactory,
        ConfigStateManager configStateManager,
        GEConfig config,
        @Named("default") ConfigValues defaultConfigValues,
        ConfigScreenBuilderFactory configScreenBuilderFactory,
        EconomyRegistry economyRegistry
    ) {
        this.translator = translatorFactory.getTranslator(GrandEconomyConstants.MODID);
        this.configStateManager = configStateManager;
        this.config = config;
        this.defaultConfigValues = defaultConfigValues;
        this.configScreenBuilderFactory = configScreenBuilderFactory;
        this.economyRegistry = economyRegistry;
    }

    public Screen getConfigScreen(Screen parent) {
        this.configScreenBuilder = configScreenBuilderFactory.create(
            translator,
            TRANSLATION_BASE + "title",
            TRANSLATION_BASE + "global",
            parent,
            () -> configStateManager.save(config)
        );
        addGlobalCategoryEntries();
        this.configScreenBuilder.startCategory(TRANSLATION_BASE + "economyHandling");
        addEconomyHandlingCategoryEntries();
        this.configScreenBuilder.startCategory(TRANSLATION_BASE + "nativeEconomy");
        addNativeEconomyCategoryEntries();

        return this.configScreenBuilder.build();
    }

    private void addNativeEconomyCategoryEntries() {
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "currencyNameSingular",
            config.getCurrencyNameSingular(),
            defaultConfigValues.getCurrencyNameSingular(),
            config::setCurrencyNameSingular,
            (byte) 0
        );
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "currencyNameMultiple",
            config.getCurrencyNameMultiple(),
            defaultConfigValues.getCurrencyNameMultiple(),
            config::setCurrencyNameMultiple,
            (byte) 0
        );
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "decimalFormattingLanguageTag",
            config.getDecimalFormattingLanguageTag(),
            defaultConfigValues.getDecimalFormattingLanguageTag(),
            config::setDecimalFormattingLanguageTag
        );
        configScreenBuilder.addDoubleField(
            OPTION_TRANSLATION_BASE + "startBalance",
            config.getStartBalance(),
            defaultConfigValues.getStartBalance(),
            config::setStartBalance,
            0,
            Double.MAX_VALUE,
            (byte) 0
        );
    }

    private void addEconomyHandlingCategoryEntries() {
        configScreenBuilder.addStringDropdown(
            OPTION_TRANSLATION_BASE + "economyHandler",
            config.getEconomyHandler(),
            defaultConfigValues.getEconomyHandler(),
            economyRegistry.getEconomyHandlers(),
            config::setEconomyHandler,
            false,
            (byte) 2,
            value ->
                economyRegistry.hasEconomyHandler(value)
                    ? Optional.empty()
                    : Optional.of(translator.getTranslatedText(OPTION_TRANSLATION_BASE + "economyHandler.err"))
        );
        configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "enforceNonNegativeBalance",
            config.isEnforceNonNegativeBalance(),
            defaultConfigValues.isEnforceNonNegativeBalance(),
            config::setEnforceNonNegativeBalance,
            (byte) 2
        );
    }

    private void addGlobalCategoryEntries() {
        configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "showBalanceOnJoin",
            config.isShowBalanceOnJoin(),
            defaultConfigValues.isShowBalanceOnJoin(),
            config::setShowBalanceOnJoin,
            (byte) 0
        );
        configScreenBuilder.addDoublePercentSlider(
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferPercent",
            config.getPvpMoneyTransferPercent(),
            defaultConfigValues.getPvpMoneyTransferPercent(),
            config::setPvpMoneyTransferPercent,
            (byte) 1,
            (byte) 1
        );
        configScreenBuilder.addDoubleField(
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferFlat",
            config.getPvpMoneyTransferFlat(),
            defaultConfigValues.getPvpMoneyTransferFlat(),
            config::setPvpMoneyTransferFlat,
            0,
            Double.MAX_VALUE
        );
        configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "basicIncome",
            config.isBasicIncome(),
            defaultConfigValues.isBasicIncome(),
            config::setBasicIncome
        );
        configScreenBuilder.addDoubleField(
            OPTION_TRANSLATION_BASE + "basicIncomeAmount",
            config.getBasicIncomeAmount(),
            defaultConfigValues.getBasicIncomeAmount(),
            config::setBasicIncomeAmount,
            0,
            Double.MAX_VALUE
        );
        configScreenBuilder.addIntField(
            OPTION_TRANSLATION_BASE + "maxIncomeSavingsDays",
            config.getMaxIncomeSavingsDays(),
            defaultConfigValues.getMaxIncomeSavingsDays(),
            config::setMaxIncomeSavingsDays,
            0,
            Integer.MAX_VALUE,
            (byte) 2
        );
    }
}
