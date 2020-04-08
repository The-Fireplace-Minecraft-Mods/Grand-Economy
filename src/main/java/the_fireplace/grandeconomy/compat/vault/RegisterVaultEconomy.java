package the_fireplace.grandeconomy.compat.vault;

import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.econhandlers.vault.VaultEconHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class RegisterVaultEconomy extends JavaPlugin implements Economy {
    @Override
    public void onEnable() {
        super.onEnable();
        if(!Lists.newArrayList("bukkit", "vault").contains(GrandEconomy.cfg.economyBridge.toLowerCase()))
            Bukkit.getServer().getServicesManager().register(Economy.class, this, this, ServicePriority.High);
        else
            //noinspection deprecation
            GrandEconomy.setEconomy(new VaultEconHandler());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getServer().getServicesManager().unregisterAll(this);
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
