package the_fireplace.grandeconomy.config;

import com.google.common.collect.Lists;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.translation.I18n;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    private static final String GLOBAL = "global";
    private static final String BRIDGING = "bridging";
    private static final String NATIVE = "native";
    private static final ModConfig DEFAULT_CONFIG = new ModConfig();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("text.config.grandeconomy.title"));

            ConfigCategory global = builder.getOrCreateCategory(new TranslatableText("text.config.grandeconomy.global"));
            global.setDescription(new StringVisitable[]{new TranslatableText("text.config.grandeconomy.global.desc")});
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            global.addEntry(entryBuilder.startStringDropdownMenu(new TranslatableText("text.config.grandeconomy.option.locale"), GrandEconomy.config.locale)
                .setSelections()
                .setSuggestionMode(false)
                .setDefaultValue(new ModConfig().locale)
                .setTooltip(genDescriptionTranslatables("text.config.grandeconomy.option.locale.desc", 4))
                .setSaveConsumer(newValue -> GrandEconomy.config.locale = newValue)
                .setErrorSupplier(value ->
                    I18n.hasLocale(value)
                        ? Optional.empty()
                        : Optional.of(new TranslatableText("text.config.grandeconomy.option.locale.err")))
                .build());
            global.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("text.config.grandeconomy.option.showBalanceOnJoin"), GrandEconomy.config.showBalanceOnJoin)
                .setDefaultValue(new ModConfig().showBalanceOnJoin)
                .setTooltip(new TranslatableText("text.config.grandeconomy.option.showBalanceOnJoin.desc"))
                .setSaveConsumer(newValue -> GrandEconomy.config.showBalanceOnJoin = newValue)
                .build());
            global.addEntry(entryBuilder.startIntSlider(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferPercent"), (int)(GrandEconomy.config.pvpMoneyTransferPercent*1000), 0, 1000)
                .setDefaultValue((int)(DEFAULT_CONFIG.pvpMoneyTransferPercent*1000))
                .setTextGetter(val -> Text.of(String.format("%.1f", val/10d)+"%"))
                .setTooltip(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferPercent.desc"))
                .setSaveConsumer(newValue -> GrandEconomy.config.pvpMoneyTransferPercent = ((double)newValue)/1000d)
                .build());
            global.addEntry(entryBuilder.startDoubleField(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferFlat"), GrandEconomy.config.pvpMoneyTransferFlat)
                .setDefaultValue(new ModConfig().pvpMoneyTransferFlat)
                .setTooltip(new TranslatableText("text.config.grandeconomy.option.pvpMoneyTransferFlat.desc"))
                .setSaveConsumer(newValue -> GrandEconomy.config.pvpMoneyTransferFlat = newValue)
                .build());

            builder.setSavingRunnable(() -> {
                GrandEconomy.config.save();
            });
            return builder.build();
        };
    }

    private static Text[] genDescriptionTranslatables(String baseKey, int count) {
        List<Text> texts = Lists.newArrayList();
        for(int i=0;i<count;i++)
            texts.add(new TranslatableText(baseKey+"["+i+"]"));
        return texts.toArray(new Text[0]);
    }
}
