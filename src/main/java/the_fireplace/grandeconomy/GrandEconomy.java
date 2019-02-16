package the_fireplace.grandeconomy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.economy.Account;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.File;

@Mod(GrandEconomy.MODID)
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    public static final String MODNAME = "Grand Economy";
    public static final String VERSION = "${version}";

    public static MinecraftServer minecraftServer;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public GrandEconomy() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverConfig(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER)
            Config.load();
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(minecraftServer.getWorld(DimensionType.OVERWORLD));
        if (file == null)
            return;

        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        GeCommands.register(event.getCommandDispatcher());
    }

    @Nullable
    private File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler))
            return null;
        return handler.getWorldDirectory();
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
