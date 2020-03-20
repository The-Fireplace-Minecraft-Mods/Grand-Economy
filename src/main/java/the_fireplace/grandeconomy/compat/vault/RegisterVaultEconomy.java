package the_fireplace.grandeconomy.compat.vault;

import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.ServicePriority;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.compat.IRegisterable;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RegisterVaultEconomy implements IRegisterable, Economy {
    private Plugin dummyPlugin;
    @Override
    public void register() {
        dummyPlugin = new Plugin() {
            @Override
            public File getDataFolder() {
                return null;
            }

            @Override
            public PluginDescriptionFile getDescription() {
                return null;
            }

            @Override
            public FileConfiguration getConfig() {
                return null;
            }

            @Override
            public InputStream getResource(String filename) {
                return null;
            }

            @Override
            public void saveConfig() {

            }

            @Override
            public void saveDefaultConfig() {

            }

            @Override
            public void saveResource(String resourcePath, boolean replace) {

            }

            @Override
            public void reloadConfig() {

            }

            @Override
            public PluginLoader getPluginLoader() {
                return null;
            }

            @Override
            public Server getServer() {
                return Bukkit.getServer();
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void onDisable() {

            }

            @Override
            public void onLoad() {

            }

            @Override
            public void onEnable() {

            }

            @Override
            public boolean isNaggable() {
                return false;
            }

            @Override
            public void setNaggable(boolean canNag) {

            }

            @Override
            public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
                return null;
            }

            @Override
            public Logger getLogger() {
                return LogManager.getLogManager().getLogger("Grand Economy API");
            }

            @Override
            public String getName() {
                return "Grand Economy API";
            }

            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                return false;
            }

            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                return null;
            }
        };
        Bukkit.getServer().getServicesManager().register(Economy.class, this, dummyPlugin, ServicePriority.High);
    }

    @Nullable
    private static UUID getId(String s) {
        try {
            Player player = Bukkit.getServer().getPlayer(s);
            if(player != null)
                return player.getUniqueId();
            return UUID.fromString(s);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean isEnabled() {
        return !Lists.newArrayList("vault", "bukkit").contains(GrandEconomyApi.getEconomyModId().toLowerCase());
    }

    @Override
    public String getName() {
        return "Grand Economy API";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return GrandEconomyApi.toString((long)v);
    }

    @Override
    public String currencyNamePlural() {
        return GrandEconomyApi.getCurrencyName(2);
    }

    @Override
    public String currencyNameSingular() {
        return GrandEconomyApi.getCurrencyName(1);
    }

    @Override
    public boolean hasAccount(String s) {
        return GrandEconomyApi.ensureAccountExists(getId(s), null);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return GrandEconomyApi.ensureAccountExists(offlinePlayer.getUniqueId(), null);
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return hasAccount(s);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String s) {
        return GrandEconomyApi.getBalance(getId(s), null);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return GrandEconomyApi.getBalance(offlinePlayer.getUniqueId(), null);
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String s, double v) {
        return getBalance(s) >= v - 0.005;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return getBalance(offlinePlayer) >= v - 0.005;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(s, v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        boolean b = GrandEconomyApi.takeFromBalance(getId(s), (long)v, null);
        return new EconomyResponse(
                (long)v,
                getBalance(s),
                b ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE,
                null
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        boolean b =  GrandEconomyApi.takeFromBalance(offlinePlayer.getUniqueId(), (long)v, null);
        return new EconomyResponse(
                (long)v,
                getBalance(offlinePlayer),
                b ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE,
                null
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        boolean b = GrandEconomyApi.addToBalance(getId(s), (long)v, null);
        return new EconomyResponse(
                (long)v,
                getBalance(s),
                b ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE,
                null
        );
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        boolean b = GrandEconomyApi.addToBalance(offlinePlayer.getUniqueId(), (long)v, null);
        return new EconomyResponse(
                (long)v,
                getBalance(offlinePlayer),
                b ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE,
                null
        );
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, getBalance(s), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse((long)v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return depositPlayer(s, v);
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return hasAccount(s);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return hasAccount(s, s1);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer, s);
    }
}
