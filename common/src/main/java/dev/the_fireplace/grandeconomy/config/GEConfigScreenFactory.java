package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.economy.EconomyAdapterRegistryImpl;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.injectables.ConfigScreenBuilderFactory;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import net.minecraft.client.gui.screens.Screen;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public final class GEConfigScreenFactory {
    private static final String TRANSLATION_BASE = "text.config." + GrandEconomyConstants.MODID + ".";
    private static final String OPTION_TRANSLATION_BASE = TRANSLATION_BASE + "option.";

    private final Translator translator;
    private final ConfigStateManager configStateManager;
    private final GEConfig config;
    private final ConfigValues defaultConfigValues;
    private final ConfigScreenBuilderFactory configScreenBuilderFactory;
    private final EconomyAdapterRegistryImpl economyAdapterRegistry;

    private ConfigScreenBuilder configScreenBuilder;

    @Inject
    public GEConfigScreenFactory(
        TranslatorFactory translatorFactory,
        ConfigStateManager configStateManager,
        GEConfig config,
        @Named("default") ConfigValues defaultConfigValues,
        ConfigScreenBuilderFactory configScreenBuilderFactory,
        EconomyAdapterRegistryImpl economyAdapterRegistry
    ) {
        this.translator = translatorFactory.getTranslator(GrandEconomyConstants.MODID);
        this.configStateManager = configStateManager;
        this.config = config;
        this.defaultConfigValues = defaultConfigValues;
        this.configScreenBuilderFactory = configScreenBuilderFactory;
        this.economyAdapterRegistry = economyAdapterRegistry;
    }

    public Screen getConfigScreen(Screen parent) {
        this.configScreenBuilder = configScreenBuilderFactory.create(
            translator,
            TRANSLATION_BASE + "title",
            TRANSLATION_BASE + "global",
            parent,
            () -> configStateManager.save(config)
        ).get();
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
            config::setCurrencyNameSingular
        ).setDescriptionRowCount((byte) 0);
        configScreenBuilder.addStringField(
            OPTION_TRANSLATION_BASE + "currencyNameMultiple",
            config.getCurrencyNameMultiple(),
            defaultConfigValues.getCurrencyNameMultiple(),
            config::setCurrencyNameMultiple
        ).setDescriptionRowCount((byte) 0);
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
            config::setStartBalance
        ).setMinimum(0.0).setDescriptionRowCount((byte) 0);
    }

    private void addEconomyHandlingCategoryEntries() {
        configScreenBuilder.addStringDropdown(
            OPTION_TRANSLATION_BASE + "economyHandler",
            config.getEconomyHandler(),
            defaultConfigValues.getEconomyHandler(),
            economyAdapterRegistry.getEconomyHandlers(),
            config::setEconomyHandler
        ).setErrorSupplier(value ->
            economyAdapterRegistry.hasEconomyHandler(value)
                ? Optional.empty()
                : Optional.of(translator.getTranslatedText(OPTION_TRANSLATION_BASE + "economyHandler.err"))
        ).setDescriptionRowCount((byte) 2);
        configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "enforceNonNegativeBalance",
            config.isEnforceNonNegativeBalance(),
            defaultConfigValues.isEnforceNonNegativeBalance(),
            config::setEnforceNonNegativeBalance
        ).setDescriptionRowCount((byte) 2);
    }

    private void addGlobalCategoryEntries() {
        configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "showBalanceOnJoin",
            config.isShowBalanceOnJoin(),
            defaultConfigValues.isShowBalanceOnJoin(),
            config::setShowBalanceOnJoin
        ).setDescriptionRowCount((byte) 0);
        configScreenBuilder.addDoubleSlider(
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferPercent",
            config.getPvpMoneyTransferPercent(),
            defaultConfigValues.getPvpMoneyTransferPercent(),
            config::setPvpMoneyTransferPercent,
            0,
            100
        ).enablePercentMode();
        configScreenBuilder.addDoubleField(
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferFlat",
            config.getPvpMoneyTransferFlat(),
            defaultConfigValues.getPvpMoneyTransferFlat(),
            config::setPvpMoneyTransferFlat
        ).setMinimum(0.0);
        OptionBuilder<Boolean> basicIncome = configScreenBuilder.addBoolToggle(
            OPTION_TRANSLATION_BASE + "basicIncome",
            config.isBasicIncome(),
            defaultConfigValues.isBasicIncome(),
            config::setBasicIncome
        );
        configScreenBuilder.addDoubleField(
            OPTION_TRANSLATION_BASE + "basicIncomeAmount",
            config.getBasicIncomeAmount(),
            defaultConfigValues.getBasicIncomeAmount(),
            config::setBasicIncomeAmount
        ).setMinimum(0.0).addDependency(basicIncome);
        configScreenBuilder.addIntField(
            OPTION_TRANSLATION_BASE + "maxIncomeSavingsDays",
            config.getMaxIncomeSavingsDays(),
            defaultConfigValues.getMaxIncomeSavingsDays(),
            config::setMaxIncomeSavingsDays
        ).setMinimum(0)
        .setDescriptionRowCount((byte) 2)
        .addDependency(basicIncome);
    }
}
