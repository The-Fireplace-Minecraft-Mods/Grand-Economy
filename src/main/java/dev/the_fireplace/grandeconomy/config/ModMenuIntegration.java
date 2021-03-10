package dev.the_fireplace.grandeconomy.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.EconomyRegistry;
import dev.the_fireplace.lib.api.client.ConfigScreenBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.StringVisitable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration extends ConfigScreenBuilder implements ModMenuApi {
    private static final ModConfig.Access DEFAULT_CONFIG = ModConfig.getDefaultData();
    private static final String TRANSLATION_BASE = "text.config.grandeconomy.";
    private static final String OPTION_TRANSLATION_BASE = TRANSLATION_BASE + "option.";

    private final EconomyRegistry economyRegistry = EconomyRegistry.getInstance();
    private final ModConfig.Access config = ModConfig.getData();

    public ModMenuIntegration() {
        super(GrandEconomy.getTranslator());
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(translator.getTranslatedText(TRANSLATION_BASE + "title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> ModConfig.getInstance().resave());
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(translator.getTranslatedText(TRANSLATION_BASE + "global"));
        global.setDescription(new StringVisitable[]{translator.getTranslatedText(TRANSLATION_BASE + "global.desc")});
        addGlobalCategoryEntries(entryBuilder, global);

        ConfigCategory economyHandling = builder.getOrCreateCategory(translator.getTranslatedText(TRANSLATION_BASE + "economyHandling"));
        economyHandling.setDescription(new StringVisitable[]{translator.getTranslatedText(TRANSLATION_BASE + "economyHandling.desc")});
        addEconomyHandlingCategoryEntries(entryBuilder, economyHandling);

        ConfigCategory nativeEconomy = builder.getOrCreateCategory(translator.getTranslatedText(TRANSLATION_BASE + "nativeEconomy"));
        nativeEconomy.setDescription(new StringVisitable[]{translator.getTranslatedText(TRANSLATION_BASE + "nativeEconomy.desc")});
        addNativeEconomyCategoryEntries(entryBuilder, nativeEconomy);
    }

    private void addNativeEconomyCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory nativeEconomy) {
        addStringField(
            entryBuilder,
            nativeEconomy,
            OPTION_TRANSLATION_BASE + "currencyNameSingular",
            config.getCurrencyNameSingular(),
            DEFAULT_CONFIG.getCurrencyNameSingular(),
            config::setCurrencyNameSingular
        );
        addStringField(
            entryBuilder,
            nativeEconomy,
            OPTION_TRANSLATION_BASE + "currencyNameMultiple",
            config.getCurrencyNameMultiple(),
            DEFAULT_CONFIG.getCurrencyNameMultiple(),
            config::setCurrencyNameMultiple
        );
        addStringField(
            entryBuilder,
            nativeEconomy,
            OPTION_TRANSLATION_BASE + "decimalFormattingLanguageTag",
            config.getDecimalFormattingLanguageTag(),
            DEFAULT_CONFIG.getDecimalFormattingLanguageTag(),
            config::setDecimalFormattingLanguageTag
        );
        addDoubleField(
            entryBuilder,
            nativeEconomy,
            OPTION_TRANSLATION_BASE + "startBalance",
            config.getStartBalance(),
            DEFAULT_CONFIG.getStartBalance(),
            config::setStartBalance,
            0,
            Double.MAX_VALUE
        );
    }

    private void addEconomyHandlingCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory economyHandlingCategory) {
        economyHandlingCategory.addEntry(
            entryBuilder.startStringDropdownMenu(translator.getTranslatedText(OPTION_TRANSLATION_BASE + "economyHandler"), config.getEconomyHandler())
            .setSelections(economyRegistry.getEconomyHandlers())
            .setSuggestionMode(false)
            .setDefaultValue(DEFAULT_CONFIG.getEconomyHandler())
            .setTooltip(genDescriptionTranslatables(OPTION_TRANSLATION_BASE + "economyHandler.desc", 2))
            .setSaveConsumer(config::setEconomyHandler)
            .setErrorSupplier(value ->
                economyRegistry.hasEconomyHandler(value)
                    ? Optional.empty()
                    : Optional.of(translator.getTranslatedText(OPTION_TRANSLATION_BASE + "economyHandler.err")))
            .build());
        addBoolToggle(
            entryBuilder,
            economyHandlingCategory,
            OPTION_TRANSLATION_BASE + "enforceNonNegativeBalance",
            config.isEnforceNonNegativeBalance(),
            DEFAULT_CONFIG.isEnforceNonNegativeBalance(),
            config::setEnforceNonNegativeBalance,
            (byte) 2
        );
    }

    private void addGlobalCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory global) {
        addBoolToggle(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "showBalanceOnJoin",
            config.isShowBalanceOnJoin(),
            DEFAULT_CONFIG.isShowBalanceOnJoin(),
            config::setShowBalanceOnJoin,
            (byte) 0
        );
        addDoublePercentSlider(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferPercent",
            config.getPvpMoneyTransferPercent(),
            DEFAULT_CONFIG.getPvpMoneyTransferPercent(),
            config::setPvpMoneyTransferPercent,
            (byte) 1,
            (byte) 1
        );
        addDoubleField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "pvpMoneyTransferFlat",
            config.getPvpMoneyTransferFlat(),
            DEFAULT_CONFIG.getPvpMoneyTransferFlat(),
            config::setPvpMoneyTransferFlat,
            0,
            Double.MAX_VALUE
        );
        addBoolToggle(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "basicIncome",
            config.isBasicIncome(),
            DEFAULT_CONFIG.isBasicIncome(),
            config::setBasicIncome
        );
        addDoubleField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "basicIncomeAmount",
            config.getBasicIncomeAmount(),
            DEFAULT_CONFIG.getBasicIncomeAmount(),
            config::setBasicIncomeAmount,
            0,
            Double.MAX_VALUE
        );
        addIntField(
            entryBuilder,
            global,
            OPTION_TRANSLATION_BASE + "maxIncomeSavingsDays",
            config.getMaxIncomeSavingsDays(),
            DEFAULT_CONFIG.getMaxIncomeSavingsDays(),
            config::setMaxIncomeSavingsDays,
            0,
            Integer.MAX_VALUE
        );
    }
}
