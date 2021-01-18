package the_fireplace.grandeconomy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.commands.*;
import the_fireplace.grandeconomy.compat.IRegisterable;
import the_fireplace.grandeconomy.compat.sponge.RegisterSpongeEconomy;
import the_fireplace.grandeconomy.earnings.ConversionItems;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;
import the_fireplace.grandeconomy.econhandlers.ep.EnderPayEconHandler;
import the_fireplace.grandeconomy.econhandlers.fe.ForgeEssentialsEconHandler;
import the_fireplace.grandeconomy.econhandlers.ge.Account;
import the_fireplace.grandeconomy.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.econhandlers.pixelmon.PixelmonEconHandler;
import the_fireplace.grandeconomy.econhandlers.sponge.SpongeEconHandler;
import the_fireplace.grandeconomy.econhandlers.te.ThutEssentialsEconHandler;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod(modid = GrandEconomy.MODID, name = GrandEconomy.MODNAME, version = GrandEconomy.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*")
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    public static final String MODNAME = "Grand Economy";
    public static final String VERSION = "${version}";

    @SuppressWarnings("deprecation")
    public static Logger LOGGER = FMLLog.getLogger();

    public static MinecraftServer minecraftServer;

    private static IEconHandler economy;
    private static final IEconHandler economyWrapper = new EconomyHandlerWrapper();
    public static IEconHandler getEconomy() {
        return economyWrapper;
    }

    @Mod.Instance(MODID)
    public static GrandEconomy instance;

    private static final Map<String, IEconHandler> econHandlers = Maps.newHashMap();

    public static boolean hasEconHandler(String key) {
        return econHandlers.containsKey(key);
    }

    public static boolean registerEconHandler(IEconHandler handler, String forModid, String... aliases) {
        if(econHandlers.containsKey(forModid) || forModid.equalsIgnoreCase(MODID))
            return false;
        econHandlers.put(forModid, handler);
        for(String alias: aliases)
            econHandlers.putIfAbsent(alias, handler);
        return true;
    }

    public static File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        configDir = new File(event.getModConfigurationDirectory(), "grandeconomy-extra");
        //noinspection ResultOfMethodCallIgnored
        configDir.mkdirs();
        //Initialize ConversionItems
        ConversionItems.hasValue(new ResourceLocation("null"), 0);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        switch(globalConfig.economyBridge.toLowerCase()) {
            case "sponge":
            case "spongeapi":
            case "spongeforge":
                economy = new SpongeEconHandler();
                break;
            case "forgeessentials":
            case "fe":
                economy = new ForgeEssentialsEconHandler();
                break;
            case "enderpay":
            case "ep":
                economy = new EnderPayEconHandler();
                break;
            case "thutessentials":
                economy = new ThutEssentialsEconHandler();
                break;
            case "vault":
            case "bukkit":
                //Wait because we will load it when the bukkit plugin loads
                break;
            case "pixelmon":
                economy = new PixelmonEconHandler();
                break;
            default:
                economy = econHandlers.getOrDefault(globalConfig.economyBridge, new GrandEconomyEconHandler());
        }
        //Null check because the Vault economy can't be initialized until later
        if(economy != null)
            getEconomy().init();
        //Make the economy we are using get registered with Sponge, if we aren't using Sponge
        if(!Lists.newArrayList("sponge", "spongeapi", "spongeforge").contains(globalConfig.economyBridge.toLowerCase())
                && Loader.isModLoaded("spongeapi")) {
            IRegisterable spongeCompat = new RegisterSpongeEconomy();
            spongeCompat.register();
        }
    }

    /**
     * @deprecated INTERNAL USE ONLY, use {@link the_fireplace.grandeconomy.api.GrandEconomyApi#registerEconomyHandler(IEconHandler, String, String...)} instead
     */
    @Deprecated
    public static void setEconomy(IEconHandler handler) {
        if(economy == null) {
            economy = handler;
            getEconomy().init();
        }
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getEntityWorld());
        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        registerCommands((ServerCommandManager)minecraftServer.getCommandManager());
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        Account.saveAll();
    }

    public static final List<CommandBase> commands = Lists.newArrayList();

    private void registerCommands(ServerCommandManager manager) {
        commands.addAll(Sets.newHashSet(
            new CommandWallet(),
            new CommandBalance(),
            new CommandConvert(),
            new CommandPay(),
            new CommandGEHelp()
        ));
        for(CommandBase command: commands)
            manager.registerCommand(command);
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler))
            return null;
        return handler.getWorldDirectory();
    }

    @Config(modid = MODID, category = "anyCurrency")
    public static class globalConfig {
        @Config.Comment("If enabled, players will be shown a message with their account balance when they join the server")
        public static boolean showBalanceOnJoin = false;
        @Config.Comment("What percentage of players money should be transferred to killer")
        @Config.RangeDouble(min=0, max=100)
        public static double pvpMoneyTransferPercent = 0;
        @Config.Comment("What amount of players money should be transferred to killer")
        @Config.RangeDouble(min=0)
        public static double pvpMoneyTransferFlat = 0;
        @Config.Comment("Which economy to bridge to, if any. Choices are \"none\", \"sponge\", \"enderpay\", \"forgeessentials\", \"thutessentials\", \"pixelmon\", and \"vault\". The game will crash if you choose one that is not loaded. If using Sponge, make sure you have a Sponge economy loaded. If using Vault, make sure the Grand Economy Vault Compat ( https://dev.bukkit.org/projects/grand-economy-vault-compat ) plugin is loaded.")
        public static String economyBridge = "none";
        @Config.Comment("Server locale - the client's locale takes precedence if Grand Economy is installed there.")
        public static String locale = "en_us";
        @Config.Comment("Makes sure account balances cannot go below zero - useful when working with plugins that don't properly prevent negative balances")
        public static boolean enforceNonNegativeBalance = true;
    }
    @Config(modid = MODID, category = "nativeCurrency")
    public static class nativeConfig {
        @Config.Comment("Currency name (Singular). This option only works when not using an economy bridge.")
        public static String currencyNameSingular = "gp";
        @Config.Comment("Currency name (Multiple). This option only works when not using an economy bridge.")
        public static String currencyNameMultiple = "gp";
        @Config.Comment("Thousands separator. This option only works when not using an economy bridge.")
        public static String thousandsSeparator = ",";
        @Config.Comment("Amount of currency given to new players when they join the server. This option only works when not using an economy bridge.")
        @Config.RangeDouble(min=0)
        public static double startBalance = 100;
        @Config.Comment("Give each player credits every day they log in. This option only works when not using an economy bridge.")
        public static boolean basicIncome = true;
        @Config.Comment("The amount of basic income to be given to a player each day.")
        @Config.RangeDouble(min=0)
        public static double basicIncomeAmount = 50;
        @Config.Comment("The max number of days since last login the player will be paid basic income for. Ex. If this option is set to 5, the mod will save income for 5 days of the player being offline, to give to the player when they log in.")
        @Config.RangeInt(min=0)
        public static int maxBasicIncomeDays = 5;
    }

    private static class EconomyHandlerWrapper implements IEconHandler {
        @Override
        public double getBalance(UUID uuid, Boolean isPlayer) {
            double balance = economy.getBalance(uuid, isPlayer);

            return Double.isNaN(balance) ? 0 : balance;
        }

        @Override
        public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
            if (Double.isNaN(amount)) {
                return false;
            }
            if (globalConfig.enforceNonNegativeBalance && amount < 0) {
                if(getBalance(uuid, isPlayer)+amount < 0)
                    return false;
            }
            return economy.addToBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
            if (Double.isNaN(amount)) {
                return false;
            }
            if(globalConfig.enforceNonNegativeBalance && amount > 0) {
                if(getBalance(uuid, isPlayer)-amount < 0)
                    return false;
            }
            return economy.takeFromBalance(uuid, amount, isPlayer);
        }

        @Override
        public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
            if (Double.isNaN(amount)) {
                return false;
            }
            if(globalConfig.enforceNonNegativeBalance && amount < 0)
                return false;
            return economy.setBalance(uuid, amount, isPlayer);
        }

        @Override
        public String getCurrencyName(double amount) {
            return economy.getCurrencyName(amount);
        }

        @Override
        public String getFormattedCurrency(double amount) {
            return economy.getFormattedCurrency(amount);
        }

        @Override
        public String getId() {
            return economy.getId();
        }

        @Override
        public void init() {
            economy.init();
        }
    }
}
