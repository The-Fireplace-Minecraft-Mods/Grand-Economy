package the_fireplace.grandeconomy;

import com.google.common.collect.Maps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
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
import the_fireplace.grandeconomy.econhandlers.ge.Account;
import the_fireplace.grandeconomy.econhandlers.ge.GrandEconomyEconHandler;
import the_fireplace.grandeconomy.econhandlers.sponge.SpongeEconHandler;
import the_fireplace.grandeconomy.econhandlers.vault.VaultEconHandler;
import the_fireplace.grandeconomy.events.NetworkEvents;

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
        MinecraftForge.EVENT_BUS.register(this);
        NetworkEvents.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
        instance = this;
    }

    public static MinecraftServer getServer() {
        return minecraftServer;
    }

    public void serverConfig(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            //Load the config
            Config.load();
        }
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        Account.clear();

        minecraftServer = event.getServer();
        configDir = new File(minecraftServer.getActiveAnvilConverter().getFile(minecraftServer.getFolderName(), "serverconfig"), "grandeconomy-extra");
        configDir.mkdirs();
        //Initialize ConversionItems
        ConversionItems.hasValue(null);
        Account.setLocation(new File(getServer().forgeGetWorldMap().get(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "GrandEconomy-accounts"));
        GeCommands.register(event.getCommandDispatcher());
    }

    //Low priority in case other mods decide to ignore the API documentation and register their economy bridges during FMLServerStartedEvent.
    //This is not foolproof, as the lifecycle events are threaded in 1.13+, so it is possible that this gets run at the same time as other mods' FMLServerStartedEvents.
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onServerStart(FMLServerStartedEvent event) {
        switch(Config.economyBridge) {
            //Do not use registerEconHandler for the built in econ handlers because we do not know at register time if the corresponding mods/plugins are loaded.
            case "sponge":
            case "spongeapi":
            case "spongeforge":
                economy = new SpongeEconHandler();
                break;
            /*case "forgeessentials":
            case "fe":
                economy = new ForgeEssentialsEconHandler();
                break;*/
            /*case "enderpay":
            case "ep":
                economy = new EnderPayEconHandler();
                break;*/
            case "vault":
            case "bukkit":
                economy = new VaultEconHandler();
                break;
            default:
                economy = econHandlers.getOrDefault(Config.economyBridge, new GrandEconomyEconHandler());
        }
        getEconomy().init();
    }
}
