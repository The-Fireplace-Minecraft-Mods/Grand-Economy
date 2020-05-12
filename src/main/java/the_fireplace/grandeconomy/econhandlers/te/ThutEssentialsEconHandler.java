package the_fireplace.grandeconomy.econhandlers.te;

import the_fireplace.grandeconomy.econhandlers.IEconHandler;
import thut.essentials.ThutEssentials;
import thut.essentials.economy.EconomyManager;

import java.util.UUID;

public class ThutEssentialsEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return EconomyManager.getBalance(uuid);
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        EconomyManager.addBalance(uuid, (int)amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        if(getBalance(uuid, isPlayer) >= amount)
            return addToBalance(uuid, -amount, isPlayer);
        return false;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        EconomyManager.setBalance(uuid, (int)amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return "";
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return String.valueOf((int)amount);
    }

    @Override
    public String getId() {
        return ThutEssentials.MODID;
    }

    @Override
    public void init() {

    }
}
