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
        return isPlayer == null && Bukkit.getOfflinePlayer(uuid).hasPlayedBefore() || isPlayer == Boolean.TRUE || !econ.hasBankSupport();
    }

    @Override
    public long getBalance(UUID uuid, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return (long) econ.getBalance(Bukkit.getOfflinePlayer(uuid));
        else
            return (long) econ.bankBalance(uuid.toString()).balance;
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return econ.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
        else
            return econ.bankDeposit(uuid.toString(), amount).transactionSuccess();
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
        else
            return econ.bankWithdraw(uuid.toString(), amount).transactionSuccess();
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer)) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            if(econ.getBalance(p) > amount)
                return econ.withdrawPlayer(p, amount-econ.getBalance(p)).transactionSuccess();
            else
                return econ.depositPlayer(p, econ.getBalance(p)-amount).transactionSuccess();
        } else {
            if(econ.bankBalance(uuid.toString()).balance > amount)
                return econ.bankWithdraw(uuid.toString(), amount-econ.bankBalance(uuid.toString()).balance).transactionSuccess();
            else
                return econ.bankDeposit(uuid.toString(), econ.bankBalance(uuid.toString()).balance-amount).transactionSuccess();
        }
    }

    @Override
    public String getCurrencyName(long amount) {
        return amount == 1 ? econ.currencyNameSingular() : econ.currencyNamePlural();
    }

    @Override
    public String toString(long amount) {
        return econ.format(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        if(shouldUsePlayerAccount(uuid, isPlayer))
            return econ.hasAccount(Bukkit.getOfflinePlayer(uuid)) || econ.createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
        else
            return econ.getBanks().contains(uuid.toString()) || econ.createBank(uuid.toString(), Bukkit.getOfflinePlayer(uuid)).transactionSuccess();
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
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        econ = economyProvider.getProvider();
    }
}
