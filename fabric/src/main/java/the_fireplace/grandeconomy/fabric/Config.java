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
    public static long pvpMoneyTransfer = 0;
    public static boolean enforceNonNegativeBalance = true;

    public static String currencyNameSingular = "gp";
    public static String currencyNameMultiple = "gp";
    public static boolean basicIncome = true;
    public static long basicIncomeAmount = 50;
    public static long startBalance = 100;
    public static byte maxBasicIncomeDays = 5;

    public static void load() {
        if(!configFile.exists())
            create();
        else
            update();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(configFile));
            if(obj instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) obj;

                JsonObject anyEconomy = jsonObject.getAsJsonObject("any-economy");
                economyBridge = getJsonPrimitive(anyEconomy, "economyBridge").getAsString();
                locale = getJsonPrimitive(anyEconomy, "locale").getAsString();
                showBalanceOnJoin = getJsonPrimitive(anyEconomy, "showBalanceOnJoin").getAsBoolean();
                pvpMoneyTransfer = getJsonPrimitive(anyEconomy, "pvpMoneyTransfer").getAsLong();
                pvpMoneyTransfer = rangeLong(pvpMoneyTransfer, Long.MIN_VALUE, 100);
                enforceNonNegativeBalance = getJsonPrimitive(anyEconomy, "enforceNonNegativeBalance").getAsBoolean();

                JsonObject nativeEconomy = jsonObject.getAsJsonObject("native-economy");
                currencyNameSingular = getJsonPrimitive(nativeEconomy, "currencyNameSingular").getAsString();
                currencyNameMultiple = getJsonPrimitive(nativeEconomy, "currencyNameMultiple").getAsString();
                basicIncome = getJsonPrimitive(nativeEconomy, "basicIncome").getAsBoolean();
                basicIncomeAmount = getJsonPrimitive(nativeEconomy, "basicIncomeAmount").getAsLong();
                basicIncomeAmount = rangeLong(basicIncomeAmount, 0, Long.MAX_VALUE);
                startBalance = getJsonPrimitive(nativeEconomy, "startBalance").getAsLong();
                startBalance = rangeLong(startBalance, 0, Long.MAX_VALUE);
                maxBasicIncomeDays = getJsonPrimitive(nativeEconomy, "maxBasicIncomeDays").getAsByte();
                maxBasicIncomeDays = rangeByte(maxBasicIncomeDays, (byte) 0, Byte.MAX_VALUE);
            } else
                GrandEconomy.LOGGER.error("Error parsing config - file was not a JsonObject.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double rangeDouble(double input, double min, double max) {
        return Math.min(max, Math.max(input, min));
    }

    private static byte rangeByte(byte input, byte min, byte max) {
        return (byte)Math.min(max, Math.max(input, min));
    }

    private static int rangeInt(int input, int min, int max) {
        return Math.min(max, Math.max(input, min));
    }

    private static long rangeLong(long input, long min, long max) {
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

    private static JsonObject createObject(JsonPrimitive value, String comment, long min, long max) {
        JsonObject obj = new JsonObject();
        obj.add("value", value);
        obj.addProperty("_comment", comment);
        obj.addProperty("_minimum", min);
        obj.addProperty("_maximum", max);
        return obj;
    }

    private static JsonObject getDefaultConfig() {
        JsonObject obj = new JsonObject();

        JsonObject anyEconomy = new JsonObject();
        anyEconomy.add("economyBridge", createObject(new JsonPrimitive(economyBridge), "Which economy to bridge to, if any. This means Grand Economy and all mods using it will use the target mod's currency. There are currently no possible options built in for Fabric. The game will crash if you choose one that is not loaded."));
        anyEconomy.add("locale", createObject(new JsonPrimitive(locale), "Server locale - the client's locale takes precedence if Grand Economy for Fabric is installed there."));
        anyEconomy.add("showBalanceOnJoin", createObject(new JsonPrimitive(showBalanceOnJoin), "If enabled, players will be shown a message with their account balance when they join the server."));
        anyEconomy.add("pvpMoneyTransfer", createObject(new JsonPrimitive(pvpMoneyTransfer), "What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer", Long.MIN_VALUE, 100));
        anyEconomy.add("enforceNonNegativeBalance", createObject(new JsonPrimitive(enforceNonNegativeBalance), "Makes sure account balances cannot go below zero - useful when working with plugins that don't properly prevent negative balances."));
        obj.add("any-economy", anyEconomy);

        JsonObject nativeEconomy = new JsonObject();
        nativeEconomy.add("currencyNameSingular", createObject(new JsonPrimitive(currencyNameSingular), "Currency name (max 20 char)"));
        nativeEconomy.add("currencyNameMultiple", createObject(new JsonPrimitive(currencyNameMultiple), "Currency name (max 20 char)"));
        nativeEconomy.add("basicIncome", createObject(new JsonPrimitive(basicIncome), "Give each player credits daily."));
        nativeEconomy.add("basicIncomeAmount", createObject(new JsonPrimitive(basicIncomeAmount), "The amount of basic income to be given to a player.", 0, Long.MAX_VALUE));
        nativeEconomy.add("startBalance", createObject(new JsonPrimitive(startBalance), "Amount of currency given to new players when they join the server", 0, Long.MAX_VALUE));
        nativeEconomy.add("maxBasicIncomeDays", createObject(new JsonPrimitive(maxBasicIncomeDays), "The max number of days since last login the player will be paid for. Ex. If this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when he/she logs in.", 0, Byte.MAX_VALUE));
        obj.add("native-economy", nativeEconomy);

        return obj;
    }

    private static void create() {
        try {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();
            FileWriter file = new FileWriter(configFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(getDefaultConfig());
            file.write(json);
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(configFile));
            if (obj instanceof JsonObject) {
                JsonObject newObj = (JsonObject) obj;
                GsonTools.extendJsonObject(newObj, GsonTools.ConflictStrategy.PREFER_FIRST_OBJ, getDefaultConfig());
                //noinspection ConstantConditions
                if(!newObj.equals(obj)) {
                    FileWriter file = new FileWriter(configFile);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(obj);
                    file.write(json);
                    file.close();
                }
            } else {
                create();
            }
        } catch(GsonTools.JsonObjectExtensionConflictException |IOException e) {
            create();
        }
    }
}
