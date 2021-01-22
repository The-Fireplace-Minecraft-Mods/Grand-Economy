package the_fireplace.grandeconomy.compat.gunpowder;

import io.github.gunpowder.api.GunpowderMod;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;
import the_fireplace.grandeconomy.api.Economy;

import java.math.BigDecimal;
import java.util.UUID;

public class GunpowderEconomy implements Economy {

    private BalanceHandler balanceHandler;

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return balanceHandler.getUser(uuid).getBalance().doubleValue();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        balanceHandler.getUser(uuid).setBalance(BigDecimal.valueOf(getBalance(uuid, isPlayer)+amount));
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (getBalance(uuid, isPlayer) > amount) {
            balanceHandler.getUser(uuid).setBalance(BigDecimal.valueOf(getBalance(uuid, isPlayer) - amount));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        balanceHandler.getUser(uuid).setBalance(BigDecimal.valueOf(amount));
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return "";
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return String.format("{%.2f}", Math.round(amount * 100) / 100.0);
    }

    @Override
    public String getId() {
        return "gunpowder-api";
    }

    @Override
    public void init() {
        balanceHandler = GunpowderMod.getInstance().getRegistry().getModelHandler(BalanceHandler.class);
    }
}
