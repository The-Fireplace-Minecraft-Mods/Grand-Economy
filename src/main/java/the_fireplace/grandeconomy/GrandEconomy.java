package the_fireplace.grandeconomy;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import the_fireplace.grandeconomy.economy.Account;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod(GrandEconomy.MODID)
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    public static final String MODNAME = "Grand Economy";
    public static final String VERSION = "${version}";

    public static MinecraftServer minecraftServer;
    public static Settings settings;
    private static ModConfig config;

    public void preInit(FMLCommonSetupEvent event) {
        config = new ModConfig(ModConfig.Type.SERVER);
        config.load();

        settings = new Settings();
        settings.loadConfig(config);
    }

    public void postInit(FMLPostInitializationEvent event) {
        config.save();
    }

    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.func_212370_w().iterator().next());
        if (file == null)
            return;

        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        //TODO register commands
    }

    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler))
            return null;
        return handler.getWorldDirectory();
    }
}
