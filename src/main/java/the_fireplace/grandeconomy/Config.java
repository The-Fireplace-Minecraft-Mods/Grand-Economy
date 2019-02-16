package the_fireplace.grandeconomy;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static String currencyNameSingular;
    public static String currencyNameMultiple;
    public static boolean showBalanceOnJoin;
    public static int pvpMoneyDrop;

    public static boolean basicIncome;
    public static int basicIncomeAmount;
    public static int startBalance;
    public static long maxBasicIncomeDays;

    public static void load() {
        currencyNameSingular = SERVER.currencyNameSingular.get();
        currencyNameMultiple = SERVER.currencyNameMultiple.get();
        showBalanceOnJoin = SERVER.showBalanceOnJoin.get();
        pvpMoneyDrop = SERVER.pvpMoneyTransfer.get();

        basicIncome = SERVER.basicIncome.get();
        basicIncomeAmount = SERVER.basicIncomeAmount.get();
        startBalance = SERVER.startBalance.get();
        maxBasicIncomeDays = SERVER.maxBasicIncomeSavings.get();
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

        ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            currencyNameSingular = builder
                    .comment("Currency name (max 20 char)")
                    .translation("Currency Name (singular)")
                    .define("currencyNameSingular", "gp", o -> o instanceof String && ((String) o).length() <= 20);
            currencyNameMultiple = builder
                    .comment("Currency name (max 20 char)")
                    .translation("Currency Name (multiple)")
                    .define("currencyNameMultiple", "gp", o -> o instanceof String && ((String) o).length() <= 20);
            showBalanceOnJoin = builder
                    .comment("If enabled, players will be shown a message with their account balance when they join the server.")
                    .translation("Show Balance On Join")
                    .define("showBalanceOnJoin", true);
            pvpMoneyTransfer = builder
                    .comment("What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer")
                    .translation("PVP Money Transfer")
                    .defineInRange("pvpMoneyTransfer", 0, Integer.MIN_VALUE, 100);
            builder.pop();

            builder.push("income");
            basicIncome = builder
                    .comment("Give each player credits every so often. The exact amount of time can be configured, and defaults to daily.")
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
                    .comment("The max number of time periods since last login the player will be paid for. Ex. If the basic income is daily, and this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when he/she logs in.")
                    .translation("Max Basic Income Savings")
                    .defineInRange("maxBasicIncomeSavings", 5, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}