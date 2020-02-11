package the_fireplace.grandeconomy.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{
    public static final ServerConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static String currencyNameSingular;
    public static String currencyNameMultiple;
    public static boolean showBalanceOnJoin;
    public static int pvpMoneyTransfer;

    public static boolean basicIncome;
    public static int basicIncomeAmount;
    public static int startBalance;
    public static int maxBasicIncomeDays;

    public static String economyBridge;
    public static String locale;

    public static void load() {
        currencyNameSingular = COMMON.currencyNameSingular.get();
        currencyNameMultiple = COMMON.currencyNameMultiple.get();
        showBalanceOnJoin = COMMON.showBalanceOnJoin.get();
        pvpMoneyTransfer = COMMON.pvpMoneyTransfer.get();

        basicIncome = COMMON.basicIncome.get();
        basicIncomeAmount = COMMON.basicIncomeAmount.get();
        startBalance = COMMON.startBalance.get();
        maxBasicIncomeDays = COMMON.maxBasicIncomeSavings.get();

        economyBridge = COMMON.economyBridge.get();
        locale = COMMON.locale.get();
    }

    public static class ServerConfig
    {
        public ForgeConfigSpec.ConfigValue<String> currencyNameSingular;
        public ForgeConfigSpec.ConfigValue<String> currencyNameMultiple;
        public ForgeConfigSpec.BooleanValue showBalanceOnJoin;
        public ForgeConfigSpec.IntValue pvpMoneyTransfer;

        public ForgeConfigSpec.BooleanValue basicIncome;
        public ForgeConfigSpec.IntValue basicIncomeAmount;
        public ForgeConfigSpec.IntValue startBalance;
        public ForgeConfigSpec.IntValue maxBasicIncomeSavings;

        public ForgeConfigSpec.ConfigValue<String> economyBridge;
        public ForgeConfigSpec.ConfigValue<String> locale;

        ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("any-economy");
            economyBridge = builder
                    .comment("Which economy to bridge to, if any. This means Grand Economy and all mods using it will use the target mod's currency. Choices are \"none\", \"sponge\", \"enderpay\", \"forgeessentials\", and \"vault\". The game will crash if you choose one that is not loaded. If using Sponge, make sure you have a Sponge economy loaded. If you are using Vault, make sure you have a Vault-compatible economy loaded.")
                    .translation("Economy Bridge")
                    .define("economyBridge", "none", o -> o instanceof String && ((String) o).length() <= 15);
            locale = builder
                    .comment("Server locale - the client's locale takes precedence if Grand Economy for Forge is installed there.")
                    .translation("Locale")
                    .define("locale", "en_us", o -> o instanceof String && ((String) o).matches("[a-z][a-z]_[a-z][a-z]"));
            showBalanceOnJoin = builder
                    .comment("If enabled, players will be shown a message with their account balance when they join the server.")
                    .translation("Show Balance On Join")
                    .define("showBalanceOnJoin", true);
            pvpMoneyTransfer = builder
                    .comment("What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer")
                    .translation("PVP Money Transfer")
                    .defineInRange("pvpMoneyTransfer", 0, Integer.MIN_VALUE, 100);
            builder.pop();

            builder.push("native-economy");
            currencyNameSingular = builder
                    .comment("Currency name (max 20 char)")
                    .translation("Currency Name (singular)")
                    .define("currencyNameSingular", "gp", o -> o instanceof String && ((String) o).length() <= 20);
            currencyNameMultiple = builder
                    .comment("Currency name (max 20 char)")
                    .translation("Currency Name (multiple)")
                    .define("currencyNameMultiple", "gp", o -> o instanceof String && ((String) o).length() <= 20);
            basicIncome = builder
                    .comment("Give each player credits daily.")
                    .translation("Enable Basic Income")
                    .define("basicIncome", true);
            basicIncomeAmount = builder
                    .comment("The amount of basic income to be given to a player.")
                    .translation("Basic Income Amount")
                    .defineInRange("basicIncomeAmount", 50, 0, Integer.MAX_VALUE);
            startBalance = builder
                    .comment("Amount of currency given to new players when they join the server")
                    .translation("Starting Account Balance")
                    .defineInRange("startBalance", 100, 0, Integer.MAX_VALUE);
            maxBasicIncomeSavings = builder
                    .comment("The max number of days since last login the player will be paid for. Ex. If this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when he/she logs in.")
                    .translation("Max Basic Income Savings")
                    .defineInRange("maxBasicIncomeSavings", 5, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}