package the_fireplace.grandeconomy.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.lib.api.io.DirectoryResolver;
import the_fireplace.lib.api.io.JsonObjectReader;
import the_fireplace.lib.api.io.JsonObjectReaderFactory;
import the_fireplace.lib.api.io.JsonWritable;

import java.io.File;

public class ModConfig implements JsonWritable {
    private static File getConfigFile() {
        return DirectoryResolver.getInstance().getConfigPath().resolve(GrandEconomy.MODID + ".json").toFile();
    }

    public boolean showBalanceOnJoin = false;
    public double pvpMoneyTransferPercent = 0;
    public double pvpMoneyTransferFlat = 0;
    public boolean basicIncome = true;
    public double basicIncomeAmount = 50;
    public int maxIncomeSavingsDays = 5;

    public String economyHandler = GrandEconomy.MODID;
    public boolean enforceNonNegativeBalance = true;

    public String currencyNameSingular = "gp";
    public String currencyNameMultiple = "gp";
    public String decimalFormattingLanguageTag = "en-US";
    public double startBalance = 100;

    public void save() {
        writeToJson(getConfigFile());
    }

    public static ModConfig load() {
        JsonObjectReader reader = JsonObjectReaderFactory.getInstance().create(getConfigFile());
        ModConfig conf = new ModConfig();
        conf.showBalanceOnJoin = reader.readBool("showBalanceOnJoin", conf.showBalanceOnJoin);
        conf.pvpMoneyTransferPercent = reader.readDouble("pvpMoneyTransferPercent", conf.pvpMoneyTransferPercent);
        conf.pvpMoneyTransferFlat = reader.readDouble("pvpMoneyTransferFlat", conf.pvpMoneyTransferFlat);
        conf.basicIncome = reader.readBool("basicIncome", conf.basicIncome);
        conf.basicIncomeAmount = reader.readDouble("basicIncomeAmount", conf.basicIncomeAmount);
        conf.maxIncomeSavingsDays = reader.readInt("maxIncomeSavingsDays", conf.maxIncomeSavingsDays);

        conf.economyHandler = reader.readString("economyHandler", conf.economyHandler);
        conf.enforceNonNegativeBalance = reader.readBool("enforceNonNegativeBalance", conf.enforceNonNegativeBalance);

        conf.currencyNameSingular = reader.readString("currencyNameSingular", conf.currencyNameSingular);
        conf.currencyNameMultiple = reader.readString("currencyNameMultiple", conf.currencyNameMultiple);
        conf.decimalFormattingLanguageTag = reader.readString("decimalFormattingLanguageTag", conf.decimalFormattingLanguageTag);
        conf.startBalance = reader.readDouble("startBalance", conf.startBalance);

        return conf;
    }

    @Override
    public JsonObject toJson() {
        return (JsonObject) new Gson().toJsonTree(this);
    }
}
