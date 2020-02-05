package the_fireplace.grandeconomy.fabric;

import com.google.gson.*;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    public static final File configFile = new File("config", GrandEconomyApi.MODID+".json");

    public static String economyBridge;
    public static String locale;
    public static boolean showBalanceOnJoin;
    public static int pvpMoneyTransfer;

    public static String currencyNameSingular;
    public static String currencyNameMultiple;
    public static boolean basicIncome;
    public static int basicIncomeAmount;
    public static int startBalance;
    public static long maxBasicIncomeDays;

    public static void load() {
        if(!configFile.exists())
            create();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(configFile));
            if(obj instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) obj;

                economyBridge = getJsonPrimitive(jsonObject, "economyBridge").getAsString();
                locale = getJsonPrimitive(jsonObject, "locale").getAsString();
                showBalanceOnJoin = getJsonPrimitive(jsonObject, "showBalanceOnJoin").getAsBoolean();
                pvpMoneyTransfer = getJsonPrimitive(jsonObject, "pvpMoneyTransfer").getAsInt();

                currencyNameSingular = getJsonPrimitive(jsonObject, "currencyNameSingular").getAsString();
                currencyNameMultiple = getJsonPrimitive(jsonObject, "currencyNameMultiple").getAsString();
                basicIncome = getJsonPrimitive(jsonObject, "basicIncome").getAsBoolean();
                basicIncomeAmount = getJsonPrimitive(jsonObject, "basicIncomeAmount").getAsInt();
                startBalance = getJsonPrimitive(jsonObject, "startBalance").getAsInt();
                maxBasicIncomeDays = getJsonPrimitive(jsonObject, "maxBasicIncomeDays").getAsLong();
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

        obj.add("economyBridge", createObject(new JsonPrimitive(economyBridge), "Which economy to bridge to, if any. This means Grand Economy and all mods using it will use the target mod's currency. There are currently no possible options built in for Fabric. The game will crash if you choose one that is not loaded."));
        obj.add("locale", createObject(new JsonPrimitive(locale), "Server locale - the client's locale takes precedence if Grand Economy for Fabric is installed there."));
        obj.add("showBalanceOnJoin", createObject(new JsonPrimitive(showBalanceOnJoin), "If enabled, players will be shown a message with their account balance when they join the server."));
        obj.add("pvpMoneyTransfer", createObject(new JsonPrimitive(pvpMoneyTransfer), "What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer"));

        obj.add("currencyNameSingular", createObject(new JsonPrimitive(currencyNameSingular), "Currency name (max 20 char)"));
        obj.add("currencyNameMultiple", createObject(new JsonPrimitive(currencyNameMultiple), "Currency name (max 20 char)"));
        obj.add("basicIncome", createObject(new JsonPrimitive(basicIncome), "Give each player credits daily."));
        obj.add("basicIncomeAmount", createObject(new JsonPrimitive(basicIncomeAmount), "The amount of basic income to be given to a player."));
        obj.add("startBalance", createObject(new JsonPrimitive(startBalance), "Amount of currency given to new players when they join the server"));
        obj.add("maxBasicIncomeDays", createObject(new JsonPrimitive(maxBasicIncomeDays), "The max number of days since last login the player will be paid for. Ex. If this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when he/she logs in."));

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
