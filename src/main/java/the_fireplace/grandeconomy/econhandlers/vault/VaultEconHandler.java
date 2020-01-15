package the_fireplace.grandeconomy.econhandlers.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class VaultEconHandler implements IEconHandler {

    private Economy econ;

    private boolean shouldUsePlayerAccount(UUID uuid, Boolean isPlayer) {
        return isPlayer == null && Bukkit.getOfflinePlayer(uuid).hasPlayedBefore() || isPlayer == Boolean.TRUE || !getEcon().hasBankSupport();
    }

    @Override
    public long getBalance(UUID uuid, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return (long) getEcon().getBalance(Bukkit.getOfflinePlayer(uuid));
        else
            return (long) getEcon().bankBalance(uuid.toString()).balance;
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return getEcon().depositPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
        else
            return getEcon().bankDeposit(uuid.toString(), amount).transactionSuccess();
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
        else
            return getEcon().bankWithdraw(uuid.toString(), amount).transactionSuccess();
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer)) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            if(getEcon().getBalance(p) > amount)
                return getEcon().withdrawPlayer(p, amount- getEcon().getBalance(p)).transactionSuccess();
            else
                return getEcon().depositPlayer(p, getEcon().getBalance(p)-amount).transactionSuccess();
        } else {
            if(getEcon().bankBalance(uuid.toString()).balance > amount)
                return getEcon().bankWithdraw(uuid.toString(), amount- getEcon().bankBalance(uuid.toString()).balance).transactionSuccess();
            else
                return getEcon().bankDeposit(uuid.toString(), getEcon().bankBalance(uuid.toString()).balance-amount).transactionSuccess();
        }
    }

    @Override
    public String getCurrencyName(long amount) {
        return amount == 1 ? getEcon().currencyNameSingular() : getEcon().currencyNamePlural();
    }

    @Override
    public String toString(long amount) {
        return getEcon().format(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return getEcon().hasAccount(Bukkit.getOfflinePlayer(uuid)) || getEcon().createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
        else
            return getEcon().getBanks().contains(uuid.toString()) || getEcon().createBank(uuid.toString(), Bukkit.getOfflinePlayer(uuid)).transactionSuccess();
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        return null;
    }

    @Override
    public String getId() {
        return "vault";
    }

    @Override
    public void init() {

    }

    private Economy getEcon() {
        if(econ == null) {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            econ = economyProvider.getProvider();
        }
        return econ;
    }
}
