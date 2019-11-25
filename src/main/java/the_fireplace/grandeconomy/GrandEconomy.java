package the_fireplace.grandeconomy;

import com.google.common.collect.Maps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.earnings.ConversionItems;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;
import the_fireplace.grandeconomy.econhandlers.ep.EnderPayEconHandler;
import the_fireplace.grandeconomy.econhandlers.fe.ForgeEssentialsEconHandler;
import the_fireplace.grandeconomy.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.econhandlers.sponge.SpongeEconHandler;
import the_fireplace.grandeconomy.econhandlers.vault.VaultEconHandler;
import the_fireplace.grandeconomy.economy.Account;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

@Mod(GrandEconomy.MODID)
public class GrandEconomy {
    public static final String MODID = "grandeconomy";
    private static MinecraftServer minecraftServer;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static GrandEconomy instance;

    public static File configDir;

    private static IEconHandler economy;
    public static IEconHandler getEconomy() {
        return economy;
    }

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

    public GrandEconomy() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
        instance = this;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    public void serverConfig(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            configDir = new File(event.getConfig().getFullPath().toFile(), "grandeconomy-extra");
            configDir.mkdirs();
            //Initialize ConversionItems
            ConversionItems.hasValue(null);
            //Load the config
            Config.load();
        }
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        File file = getWorldDir(getServer().getWorld(DimensionType.OVERWORLD));
        if (file == null)
            return;

        Account.setLocation(new File(file, "GrandEconomy-accounts"));

        GeCommands.register(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartedEvent event) {
        switch(Config.economyBridge) {
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
            case "vault":
            case "bukkit":
                economy = new VaultEconHandler();
                break;
            default:
                economy = econHandlers.getOrDefault(Config.economyBridge, new GrandEconomyEconHandler());
        }
        getEconomy().init();
    }

    @Nullable
    private File getWorldDir(World world) {
        if (!(world instanceof ServerWorld))
            return null;
        return ((ServerWorld) world).getSaveHandler().getWorldDirectory();
    }
}
