package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.EconomyRegistry;
import dev.the_fireplace.lib.api.client.ConfigScreenBuilder;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration extends ConfigScreenBuilder implements ModMenuApi {
    private static final ModConfig.Access DEFAULT_CONFIG = ModConfig.getDefaultData();
    
    private final EconomyRegistry economyRegistry = EconomyRegistry.getInstance();
    private final ModConfig.Access config = ModConfig.getData();

    public ModMenuIntegration() {
        super(GrandEconomy.getTranslator());
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getModId() {
        return GrandEconomy.MODID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(translator.getTranslatedString("text.config.grandeconomy.title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> ModConfig.getInstance().resave());
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(translator.getTranslatedString("text.config.grandeconomy.global"));
        addGlobalCategoryEntries(entryBuilder, global);

        ConfigCategory economyHandling = builder.getOrCreateCategory(translator.getTranslatedString("text.config.grandeconomy.economyHandling"));
        addEconomyHandlingCategoryEntries(entryBuilder, economyHandling);

        ConfigCategory nativeEconomy = builder.getOrCreateCategory(translator.getTranslatedString("text.config.grandeconomy.nativeEconomy"));
        addNativeEconomyCategoryEntries(entryBuilder, nativeEconomy);
    }

    private void addNativeEconomyCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory nativeEconomy) {
        addStringField(
            entryBuilder,
            nativeEconomy,
            "text.config.grandeconomy.option.currencyNameSingular",
            config.getCurrencyNameSingular(),
            DEFAULT_CONFIG.getCurrencyNameSingular(),
            config::setCurrencyNameSingular
        );
        addStringField(
            entryBuilder,
            nativeEconomy,
            "text.config.grandeconomy.option.currencyNameMultiple",
            config.getCurrencyNameMultiple(),
            DEFAULT_CONFIG.getCurrencyNameMultiple(),
            config::setCurrencyNameMultiple
        );
        addStringField(
            entryBuilder,
            nativeEconomy,
            "text.config.grandeconomy.option.decimalFormattingLanguageTag",
            config.getDecimalFormattingLanguageTag(),
            DEFAULT_CONFIG.getDecimalFormattingLanguageTag(),
            config::setDecimalFormattingLanguageTag
        );
        addDoubleField(
            entryBuilder,
            nativeEconomy,
            "text.config.grandeconomy.option.startBalance",
            config.getStartBalance(),
            DEFAULT_CONFIG.getStartBalance(),
            config::setStartBalance,
            0,
            Double.MAX_VALUE
        );
    }

    private void addEconomyHandlingCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory economyHandlingCategory) {
        economyHandlingCategory.addEntry(
            entryBuilder.startStringDropdownMenu(translator.getTranslatedString("text.config.grandeconomy.option.economyHandler"), config.getEconomyHandler())
            .setSelections(economyRegistry.getEconomyHandlers())
            .setSuggestionMode(false)
            .setDefaultValue(DEFAULT_CONFIG.getEconomyHandler())
            .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.economyHandler.desc", 2))
            .setSaveConsumer(config::setEconomyHandler)
            .setErrorSupplier(value ->
                economyRegistry.hasEconomyHandler(value)
                    ? Optional.empty()
                    : Optional.of(translator.getTranslatedString("text.config.grandeconomy.option.economyHandler.err")))
            .build());
        economyHandlingCategory.addEntry(entryBuilder.startBooleanToggle(translator.getTranslatedString("text.config.grandeconomy.option.enforceNonNegativeBalance"), config.isEnforceNonNegativeBalance())
            .setDefaultValue(DEFAULT_CONFIG.isEnforceNonNegativeBalance())
            .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.enforceNonNegativeBalance.desc", 2))
            .setSaveConsumer(config::setEnforceNonNegativeBalance)
            .build());
    }

    private void addGlobalCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory global) {
        global.addEntry(entryBuilder.startBooleanToggle(translator.getTranslatedString("text.config.grandeconomy.option.showBalanceOnJoin"), config.isShowBalanceOnJoin())
            .setDefaultValue(DEFAULT_CONFIG.isShowBalanceOnJoin())
            .setSaveConsumer(config::setShowBalanceOnJoin)
            .build());
        addDoublePercentSlider(
            entryBuilder,
            global,
            "text.config.grandeconomy.option.pvpMoneyTransferPercent",
            config.getPvpMoneyTransferPercent(),
            DEFAULT_CONFIG.getPvpMoneyTransferPercent(),
            config::setPvpMoneyTransferPercent,
            (byte)1,
            (byte)1
        );
        addDoubleField(
            entryBuilder,
            global,
            "text.config.grandeconomy.option.pvpMoneyTransferFlat",
            config.getPvpMoneyTransferFlat(),
            DEFAULT_CONFIG.getPvpMoneyTransferFlat(),
            config::setPvpMoneyTransferFlat,
            0,
            Double.MAX_VALUE
        );
        global.addEntry(entryBuilder.startBooleanToggle(translator.getTranslatedString("text.config.grandeconomy.option.basicIncome"), config.isBasicIncome())
            .setDefaultValue(DEFAULT_CONFIG.isBasicIncome())
            .setTooltip(translator.getTranslatedString("text.config.grandeconomy.option.basicIncome.desc"))
            .setSaveConsumer(config::setBasicIncome)
            .build());
        addDoubleField(
            entryBuilder,
            global,
            "text.config.grandeconomy.option.basicIncomeAmount",
            config.getBasicIncomeAmount(),
            DEFAULT_CONFIG.getBasicIncomeAmount(),
            config::setBasicIncomeAmount,
            0,
            Double.MAX_VALUE
        );
        addIntField(
            entryBuilder,
            global,
            "text.config.grandeconomy.option.maxIncomeSavingsDays",
            config.getMaxIncomeSavingsDays(),
            DEFAULT_CONFIG.getMaxIncomeSavingsDays(),
            config::setMaxIncomeSavingsDays,
            0,
            Integer.MAX_VALUE
        );
    }
}
