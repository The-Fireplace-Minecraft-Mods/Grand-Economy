package the_fireplace.grandeconomy.config;

import com.google.common.collect.Lists;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.*;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.EconomyRegistry;
import the_fireplace.lib.api.chat.TextStyles;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    private static final ModConfig DEFAULT_CONFIG = new ModConfig();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("text.config.grandeconomy.title"));

            buildConfigCategories(builder);

            builder.setSavingRunnable(() -> GrandEconomy.getConfig().save());
            return builder.build();
        };
    }

    private void buildConfigCategories(ConfigBuilder builder) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory global = builder.getOrCreateCategory(new TranslatableText("text.config.grandeconomy.global"));
        global.setDescription(new StringVisitable[]{new TranslatableText("text.config.grandeconomy.global.desc")});
        addGlobalCategoryEntries(entryBuilder, global);

        ConfigCategory economyHandling = builder.getOrCreateCategory(new TranslatableText("text.config.grandeconomy.economyHandling"));
        economyHandling.setDescription(new StringVisitable[]{new TranslatableText("text.config.grandeconomy.economyHandling.desc")});
        addEconomyHandlingCategoryEntries(entryBuilder, economyHandling);

        ConfigCategory nativeEconomy = builder.getOrCreateCategory(new TranslatableText("text.config.grandeconomy.nativeEconomy"));
        nativeEconomy.setDescription(new StringVisitable[]{new TranslatableText("text.config.grandeconomy.nativeEconomy.desc")});
        addNativeEconomyCategoryEntries(entryBuilder, nativeEconomy);
    }

    private void addNativeEconomyCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory nativeEconomy) {
        nativeEconomy.addEntry(entryBuilder.startStrField(new TranslatableText("text.config.grandeconomy.option.currencyNameSingular"), GrandEconomy.getConfig().currencyNameSingular)
            .setDefaultValue(new ModConfig().currencyNameSingular)
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().currencyNameSingular = newValue)
            .build());
        nativeEconomy.addEntry(entryBuilder.startStrField(new TranslatableText("text.config.grandeconomy.option.currencyNameMultiple"), GrandEconomy.getConfig().currencyNameMultiple)
            .setDefaultValue(new ModConfig().currencyNameMultiple)
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().currencyNameMultiple = newValue)
            .build());
        nativeEconomy.addEntry(entryBuilder.startStrField(new TranslatableText("text.config.grandeconomy.option.decimalFormattingLanguageTag")
                .setStyle(TextStyles.BLUE.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html"))),
                GrandEconomy.getConfig().decimalFormattingLanguageTag
            )
            .setDefaultValue(new ModConfig().decimalFormattingLanguageTag)
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().decimalFormattingLanguageTag = newValue)
            .build());
        nativeEconomy.addEntry(entryBuilder.startDoubleField(new TranslatableText("text.config.grandeconomy.option.startBalance"), GrandEconomy.getConfig().startBalance)
            .setDefaultValue(new ModConfig().startBalance)
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().startBalance = newValue)
            .build());
    }

    private void addEconomyHandlingCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory economyHandlingCategory) {
        economyHandlingCategory.addEntry(entryBuilder.startStringDropdownMenu(new TranslatableText("text.config.grandeconomy.option.economyHandler"), GrandEconomy.getConfig().economyHandler)
            .setSelections(EconomyRegistry.getInstance().getEconomyHandlers())
            .setSuggestionMode(false)
            .setDefaultValue(new ModConfig().economyHandler)
            .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.economyHandler.desc", 2))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().economyHandler = newValue)
            .setErrorSupplier(value ->
                EconomyRegistry.getInstance().hasEconomyHandler(value)
                    ? Optional.empty()
                    : Optional.of(new TranslatableText("text.config.grandeconomy.option.economyHandler.err")))
            .build());
        economyHandlingCategory.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("text.config.grandeconomy.option.enforceNonNegativeBalance"), GrandEconomy.getConfig().enforceNonNegativeBalance)
            .setDefaultValue(new ModConfig().enforceNonNegativeBalance)
            .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.enforceNonNegativeBalance.desc", 2))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().enforceNonNegativeBalance = newValue)
            .build());
    }

    private void addGlobalCategoryEntries(ConfigEntryBuilder entryBuilder, ConfigCategory global) {
        global.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("text.config.grandeconomy.option.showBalanceOnJoin"), GrandEconomy.getConfig().showBalanceOnJoin)
            .setDefaultValue(new ModConfig().showBalanceOnJoin)
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().showBalanceOnJoin = newValue)
            .build());
        global.addEntry(entryBuilder.startIntSlider(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferPercent"), (int)(GrandEconomy.getConfig().pvpMoneyTransferPercent*1000), 0, 1000)
            .setDefaultValue((int)(DEFAULT_CONFIG.pvpMoneyTransferPercent*1000))
            .setTextGetter(val -> Text.of(String.format("%.1f", val/10d)+"%"))
            .setTooltip(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferPercent.desc"))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().pvpMoneyTransferPercent = ((double)newValue)/1000d)
            .build());
        global.addEntry(entryBuilder.startDoubleField(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferFlat"), GrandEconomy.getConfig().pvpMoneyTransferFlat)
            .setDefaultValue(new ModConfig().pvpMoneyTransferFlat)
            .setTooltip(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferFlat.desc"))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().pvpMoneyTransferFlat = newValue)
            .build());
        global.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("text.config.grandeconomy.option.basicIncome"), GrandEconomy.getConfig().basicIncome)
            .setDefaultValue(new ModConfig().basicIncome)
            .setTooltip(new TranslatableText("text.config.grandeconomy.option.basicIncome.desc"))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().basicIncome = newValue)
            .build());
        global.addEntry(entryBuilder.startDoubleField(new TranslatableText("text.config.grandeconomy.option.basicIncomeAmount"), GrandEconomy.getConfig().basicIncomeAmount)
            .setDefaultValue(new ModConfig().basicIncomeAmount)
            .setTooltip(new TranslatableText("text.config.grandeconomy.option.basicIncomeAmount.desc"))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().basicIncomeAmount = newValue)
            .build());
        global.addEntry(entryBuilder.startIntField(new TranslatableText("text.config.grandeconomy.option.maxIncomeSavingsDays"), GrandEconomy.getConfig().maxIncomeSavingsDays)
            .setDefaultValue(new ModConfig().maxIncomeSavingsDays)
            .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.maxIncomeSavingsDays.desc", 2))
            .setSaveConsumer(newValue -> GrandEconomy.getConfig().maxIncomeSavingsDays = newValue)
            .build());
    }

    private static Text[] genDescriptionTranslatables(String baseKey, int count) {
        List<Text> texts = Lists.newArrayList();
        for(int i=0;i<count;i++)
            texts.add(new TranslatableText(baseKey+"["+i+"]"));
        return texts.toArray(new Text[0]);
    }
}
