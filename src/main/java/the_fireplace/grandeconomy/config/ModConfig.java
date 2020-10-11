package the_fireplace.grandeconomy.config;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Jankson;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.JsonGrammar;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.JsonObject;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.impl.SyntaxError;
import the_fireplace.grandeconomy.GrandEconomy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final File configDir = new File("config");
    private static final File baseConfigFile = new File(configDir, GrandEconomy.MODID+".json5");

    public String locale = "en_us";
    public boolean showBalanceOnJoin = false;
    public double pvpMoneyTransferPercent = 0;
    public double pvpMoneyTransferFlat = 0;

    public String economyBridge = "none";
    public boolean enforceNonNegativeBalance = true;

    public String currencyNameSingular = "gp";
    public String currencyNameMultiple = "gp";
    public String thousandsSeparator = ",";
    public double startBalance = 100;
    public boolean basicIncome = true;
    public double basicIncomeAmount = 50;
    public int maxIncomeSavingsDays = 5;

    public void save() {
        try {
            FileWriter fw = new FileWriter(baseConfigFile);
            fw.write(Jankson.builder().build().toJson(this).toJson(JsonGrammar.JSON5));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static ModConfig load() {
        JsonObject obj;
        ModConfig conf = new ModConfig();
        try {
            obj = Jankson.builder().build().load(baseConfigFile);
        } catch(FileNotFoundException e) {
            return conf;
        } catch (IOException | SyntaxError | NullPointerException e) {
            e.printStackTrace();
            return conf;
        }
        if(obj.containsKey("locale"))
            conf.locale = obj.get(String.class, "locale");
        if(obj.containsKey("showBalanceOnJoin"))
            conf.showBalanceOnJoin = obj.get(Boolean.class, "showBalanceOnJoin");
        if(obj.containsKey("pvpMoneyTransferPercent"))
            conf.pvpMoneyTransferPercent = obj.get(Double.class, "pvpMoneyTransferPercent");
        if(obj.containsKey("pvpMoneyTransferFlat"))
            conf.pvpMoneyTransferFlat = obj.get(Double.class, "pvpMoneyTransferFlat");

        if(obj.containsKey("economyBridge"))
            conf.economyBridge = obj.get(String.class, "economyBridge");
        if(obj.containsKey("enforceNonNegativeBalance"))
            conf.enforceNonNegativeBalance = obj.get(Boolean.class, "enforceNonNegativeBalance");

        if(obj.containsKey("currencyNameSingular"))
            conf.currencyNameSingular = obj.get(String.class, "currencyNameSingular");
        if(obj.containsKey("currencyNameMultiple"))
            conf.currencyNameMultiple = obj.get(String.class, "currencyNameMultiple");
        if(obj.containsKey("thousandsSeparator"))
            conf.thousandsSeparator = obj.get(String.class, "thousandsSeparator");
        if(obj.containsKey("startBalance"))
            conf.startBalance = obj.get(Double.class, "startBalance");
        if(obj.containsKey("basicIncome"))
            conf.basicIncome = obj.get(Boolean.class, "basicIncome");
        if(obj.containsKey("basicIncomeAmount"))
            conf.basicIncomeAmount = obj.get(Double.class, "basicIncomeAmount");
        if(obj.containsKey("maxIncomeSavingsDays"))
            conf.maxIncomeSavingsDays = obj.get(Integer.class, "maxIncomeSavingsDays");

        return conf;
    }
}
