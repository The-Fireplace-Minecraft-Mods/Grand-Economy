package the_fireplace.grandeconomy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.commands.*;
import the_fireplace.grandeconomy.earnings.ConversionItems;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;
import the_fireplace.grandeconomy.econhandlers.ep.EnderPayEconHandler;
import the_fireplace.grandeconomy.econhandlers.fe.ForgeEssentialsEconHandler;
import the_fireplace.grandeconomy.econhandlers.ge.Account;
import the_fireplace.grandeconomy.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.econhandlers.sponge.SpongeEconHandler;

import java.io.File;
import java.util.List;
import java.util.Map;

@Mod(modid = GrandEconomy.MODID, name = GrandEconomy.MODNAME, version = GrandEconomy.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*")
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    public static final String MODNAME = "Grand Economy";
    public static final String VERSION = "${version}";

    public static Logger LOGGER = FMLLog.getLogger();

    public static MinecraftServer minecraftServer;

    public static IEconHandler economy;

    @Mod.Instance(MODID)
    public static GrandEconomy instance;

    public static File configDir;

    private static Map<String, IEconHandler> econHandlers = Maps.newHashMap();

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        configDir = new File(event.getModConfigurationDirectory(), "grandeconomy-extra");
        //noinspection ResultOfMethodCallIgnored
        configDir.mkdirs();
        //Initialize ConversionItems
        ConversionItems.hasValue(null, 0);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        switch(cfg.economyBridge) {
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
            default:
                economy = econHandlers.getOrDefault(cfg.economyBridge, new GrandEconomyEconHandler());
        }
        economy.init();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getEntityWorld());
        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        registerCommands((ServerCommandManager)minecraftServer.getCommandManager());
    }

    public static final List<CommandBase> commands = Lists.newArrayList();

    private void registerCommands(ServerCommandManager manager) {
        commands.addAll(Sets.newHashSet(
                new CommandWallet(),
                new CommandBalance(),
                new CommandPay(),
                new CommandConvert(),
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

    @Config(modid = MODID)
    public static class cfg {
        @Config.Comment("If enabled, players will be shown a message with their account balance when they join the server")
        public static boolean showBalanceOnJoin = true;
        @Config.Comment("What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer")
        @Config.RangeInt(max=100)
        public static int pvpMoneyTransfer = 0;
        @Config.Comment("Which economy to bridge to, if any. Choices are \"none\", \"sponge\", \"enderpay\", and \"forgeessentials\". The game will crash if you choose one that is not loaded. If using Sponge, make sure you have a Sponge economy loaded.")
        public static String economyBridge = "none";
        @Config.Comment("Server locale - the client's locale takes precedence if Grand Economy is installed there.")
        public static String locale = "en_us";

        @Config.Comment("Currency name (Singular). This option only works when not using an economy bridge.")
        public static String currencyNameSingular = "gp";
        @Config.Comment("Currency name (Multiple). This option only works when not using an economy bridge.")
        public static String currencyNameMultiple = "gp";

        @Config.Comment("Amount of currency given to new players when they join the server. This option only works when not using an economy bridge.")
        @Config.RangeInt(min=0)
        public static int startBalance = 100;
        @Config.Comment("Give each player credits every day they log in. This option only works when not using an economy bridge.")
        public static boolean basicIncome = true;
        @Config.Comment("The amount of basic income to be given to a player")
        @Config.RangeInt(min=0)
        public static int basicIncomeAmount = 50;
        @Config.Comment("The max number of days since last login the player will be paid basic income for")
        @Config.RangeInt(min=0)
        public static int maxBasicIncomeDays = 5;
    }
}
