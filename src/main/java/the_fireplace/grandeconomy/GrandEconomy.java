package the_fireplace.grandeconomy;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.commands.CommandBalance;
import the_fireplace.grandeconomy.commands.CommandConvert;
import the_fireplace.grandeconomy.commands.CommandPay;
import the_fireplace.grandeconomy.commands.CommandWallet;
import the_fireplace.grandeconomy.earnings.ConversionItems;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;
import the_fireplace.grandeconomy.econhandlers.ep.EnderPayEconHandler;
import the_fireplace.grandeconomy.econhandlers.fe.ForgeEssentialsEconHandler;
import the_fireplace.grandeconomy.econhandlers.ge.Account;
import the_fireplace.grandeconomy.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.econhandlers.sponge.SpongeEconHandler;

import java.io.File;

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        configDir = new File(event.getModConfigurationDirectory(), "grandeconomy-extra");
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
                economy = new GrandEconomyEconHandler();
        }
        economy.init();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getEntityWorld());
        if (file == null)
            return;

        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        registerCommands(event);
    }

    private void registerCommands(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new CommandWallet());
        manager.registerCommand(new CommandBalance());
        manager.registerCommand(new CommandPay());
        manager.registerCommand(new CommandConvert());
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler))
            return null;
        return handler.getWorldDirectory();
    }

    @Config(modid = MODID)
    public static class cfg {
        @Config.Comment("Currency name (Singular)")
        public static String currencyNameSingular = "gp";
        @Config.Comment("Currency name (Multiple)")
        public static String currencyNameMultiple = "gp";
        @Config.Comment("If enabled, players will be shown a message with their account balance when they join the server")
        public static boolean showBalanceOnJoin = true;
        @Config.Comment("What percentage (0-100) or what amount (pvpMoneyTransfer<0) of players money should be transferred to killer")
        @Config.RangeInt(max=100)
        public static int pvpMoneyTransfer = 0;
        @Config.Comment("Which economy to bridge to, if any. Choices are \"sponge\", \"enderpay\", and \"forgeessentials\". The game will crash if you choose one that is not loaded. If using Sponge, make sure you have a Sponge economy loaded.")
        public static String economyBridge = "none";

        @Config.Comment("Give each player credits every day they log in")
        public static boolean basicIncome = true;
        @Config.Comment("The amount of basic income to be given to a player")
        @Config.RangeInt(min=0)
        public static int basicIncomeAmount = 50;
        @Config.Comment("Amount of currency given to new players when they join the server")
        @Config.RangeInt(min=0)
        public static int startBalance = 100;
        @Config.Comment("The max number of days since last login the player will be paid basic income for")
        @Config.RangeInt(min=0)
        public static int maxBasicIncomeDays = 5;
    }
}
