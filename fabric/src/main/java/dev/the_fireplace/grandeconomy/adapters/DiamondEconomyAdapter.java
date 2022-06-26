package dev.the_fireplace.grandeconomy.adapters;

import com.gmail.sneakdevs.diamondeconomy.DiamondEconomy;
import com.gmail.sneakdevs.diamondeconomy.sql.DatabaseManager;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;

import java.util.UUID;

public final class DiamondEconomyAdapter implements EconomyAdapter
{
    private DatabaseManager databaseManager;

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return databaseManager.getBalanceFromUUID(uuid.toString());
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        int previousBalance = databaseManager.getBalanceFromUUID(uuid.toString());
        int newBalance = previousBalance + (int) amount;
        databaseManager.setBalance(uuid.toString(), newBalance);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        int previousBalance = databaseManager.getBalanceFromUUID(uuid.toString());
        int newBalance = Math.max(previousBalance - (int) amount, 0);
        databaseManager.setBalance(uuid.toString(), newBalance);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        databaseManager.setBalance(uuid.toString(), (int) amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return (int) amount != 1 ? "diamonds" : "diamond";
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return (int) amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return com.gmail.sneakdevs.diamondeconomy.DiamondEconomy.MODID;
    }

    @Override
    public void init() {
        this.databaseManager = DiamondEconomy.getDatabaseManager();
    }
}
