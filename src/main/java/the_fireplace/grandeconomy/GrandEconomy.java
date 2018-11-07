package the_fireplace.grandeconomy;

import the_fireplace.grandeconomy.commands.CommandBalance;
import the_fireplace.grandeconomy.commands.CommandPay;
import the_fireplace.grandeconomy.commands.CommandWallet;
import the_fireplace.grandeconomy.economy.Account;
import the_fireplace.grandeconomy.proxy.Proxy;
import the_fireplace.grandeconomy.proxy.Settings;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = GrandEconomy.MODID, name = GrandEconomy.MODNAME, version = GrandEconomy.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*")
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    public static final String MODNAME = "Grand Economy";
    public static final String VERSION = "${version}";
    @Mod.Instance(MODID)
    @SuppressWarnings("unused")
    public static GrandEconomy instance;

    public static MinecraftServer minecraftServer;
    @SidedProxy(serverSide = "the_fireplace.grandeconomy.proxy.Proxy")
    @SuppressWarnings("unused")
    public static Proxy proxy;
    @SidedProxy(serverSide = "the_fireplace.grandeconomy.proxy.Settings")
    @SuppressWarnings("unused")
    public static Settings settings;
    private static Configuration config;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        settings.loadConfig(config);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        config.save();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getEntityWorld());
        if (file == null) return;

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
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler)) return null;
        return handler.getWorldDirectory();
    }
}
