package the_fireplace.grandeconomy.fabric;

import com.google.gson.*;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    public static final File configFile = new File("config", GrandEconomyApi.MODID+".json");

    public static String economyBridge = "none";
    public static String locale = "en_us";
    public static boolean showBalanceOnJoin = false;
    public static int pvpMoneyTransfer = 0;

    public static String currencyNameSingular = "gp";
    public static String currencyNameMultiple = "gp";
    public static boolean basicIncome = true;
    public static int basicIncomeAmount = 50;
    public static int startBalance = 100;
    public static int maxBasicIncomeDays = 5;

    public static void load() {
        if(!configFile.exists())
            create();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(configFile));
            if(obj instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) obj;

                JsonObject anyEconomy = jsonObject.getAsJsonObject("any-economy");
                economyBridge = getJsonPrimitive(anyEconomy, "economyBridge").getAsString();
                locale = getJsonPrimitive(anyEconomy, "locale").getAsString();
                showBalanceOnJoin = getJsonPrimitive(anyEconomy, "showBalanceOnJoin").getAsBoolean();
                pvpMoneyTransfer = getJsonPrimitive(anyEconomy, "pvpMoneyTransfer").getAsInt();

                JsonObject nativeEconomy = jsonObject.getAsJsonObject("native-economy");
                currencyNameSingular = getJsonPrimitive(nativeEconomy, "currencyNameSingular").getAsString();
                currencyNameMultiple = getJsonPrimitive(nativeEconomy, "currencyNameMultiple").getAsString();
                basicIncome = getJsonPrimitive(nativeEconomy, "basicIncome").getAsBoolean();
                basicIncomeAmount = getJsonPrimitive(nativeEconomy, "basicIncomeAmount").getAsInt();
                startBalance = getJsonPrimitive(nativeEconomy, "startBalance").getAsInt();
                maxBasicIncomeDays = getJsonPrimitive(nativeEconomy, "maxBasicIncomeDays").getAsInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double rangeDouble(double input, double min, double max) {
        return Math.min(max, Math.max(input, min));
    }

    private static int rangeInt(int input, int min, int max) {
        return Math.min(max, Math.max(input, min));
    }

    private static JsonPrimitive getJsonPrimitive(JsonObject obj, String key) {
        return obj.getAsJsonObject(key).get("value").getAsJsonPrimitive();
    }

    private static JsonObject createObject(JsonPrimitive value, String comment) {
        JsonObject obj = new JsonObject();
        obj.add("value", value);
        obj.addProperty("_comment", comment);
        return obj;
    }

    private static void create() {
        JsonObject obj = new JsonObject();

        JsonObject anyEconomy = new JsonObject();
        anyEconomy.add("economyBridge", createObject(new JsonPrimitive(economyBridge), "Which economy to bridge to, if any. This means Grand Economy and all mods using it will use the target mod's currency. There are currently no possible options built in for Fabric. The game will crash if you choose one that is not loaded."));
        anyEconomy.add("locale", createObject(new JsonPrimitive(locale), "Server locale - the client's locale takes precedence if Grand Economy for Fabric is installed there."));
        anyEconomy.add("showBalanceOnJoin", createObject(new JsonPrimitive(showBalanceOnJoin), "If enabled, players will be shown a message with their account balance when they join the server."));
        anyEconomy.add("pvpMoneyTransfer", createObject(new JsonPrimitive(pvpMoneyTransfer), "What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer"));
        obj.add("any-economy", anyEconomy);

        JsonObject nativeEconomy = new JsonObject();
        nativeEconomy.add("currencyNameSingular", createObject(new JsonPrimitive(currencyNameSingular), "Currency name (max 20 char)"));
        nativeEconomy.add("currencyNameMultiple", createObject(new JsonPrimitive(currencyNameMultiple), "Currency name (max 20 char)"));
        nativeEconomy.add("basicIncome", createObject(new JsonPrimitive(basicIncome), "Give each player credits daily."));
        nativeEconomy.add("basicIncomeAmount", createObject(new JsonPrimitive(basicIncomeAmount), "The amount of basic income to be given to a player."));
        nativeEconomy.add("startBalance", createObject(new JsonPrimitive(startBalance), "Amount of currency given to new players when they join the server"));
        nativeEconomy.add("maxBasicIncomeDays", createObject(new JsonPrimitive(maxBasicIncomeDays), "The max number of days since last login the player will be paid for. Ex. If this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when he/she logs in."));
        obj.add("native-economy", nativeEconomy);

        try {
            FileWriter file = new FileWriter(configFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(obj);
            file.write(json);
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
