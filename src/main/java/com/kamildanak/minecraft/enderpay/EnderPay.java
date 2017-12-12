package com.kamildanak.minecraft.enderpay;

import com.kamildanak.minecraft.enderpay.commands.CommandBalance;
import com.kamildanak.minecraft.enderpay.commands.CommandPay;
import com.kamildanak.minecraft.enderpay.commands.CommandWallet;
import com.kamildanak.minecraft.enderpay.economy.Account;
import com.kamildanak.minecraft.enderpay.events.EventHandler;
import com.kamildanak.minecraft.enderpay.proxy.Proxy;
import com.kamildanak.minecraft.enderpay.proxy.Settings;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(modid = EnderPay.modID, name = EnderPay.modName, version = EnderPay.VERSION, acceptedMinecraftVersions = EnderPay.ACCEPTED_VERSIONS, serverSideOnly = true, acceptableRemoteVersions = "*")
public class EnderPay {
    public static final String modID = "enderpayserver";
    static final String VERSION = "{@enderPayVersion}";
    static final String ACCEPTED_VERSIONS = "[1.12,1.13)";
    static final String modName = "EnderPay Economy Api - Server Edition";
    @Mod.Instance(modID)
    @SuppressWarnings("unused")
    public static EnderPay instance;

    public static MinecraftServer minecraftServer;
    @SidedProxy(serverSide = "com.kamildanak.minecraft.enderpay.proxy.Proxy")
    @SuppressWarnings("unused")
    public static Proxy proxy;
    @SidedProxy(serverSide = "com.kamildanak.minecraft.enderpay.proxy.Settings")
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
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
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

        Account.setLocation(new File(file, "EnderPay-accounts"));

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
