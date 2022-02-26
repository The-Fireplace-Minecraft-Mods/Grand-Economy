package dev.the_fireplace.grandeconomy.economy;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.BalanceChangedTrigger;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.EconomySelectedTrigger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Implementation
@Singleton
public final class EconomyImplementationWrapper implements Economy
{
    private final ConfigValues configValues;
    private final EconomySelectedTrigger economySelectedTrigger;
    private final BalanceChangedTrigger balanceChangedTrigger;
    private EconomyAdapter economyAdapter = null;

    @Inject
    public EconomyImplementationWrapper(ConfigValues configValues, EconomySelectedTrigger economySelectedTrigger, BalanceChangedTrigger balanceChangedTrigger) {
        this.configValues = configValues;
        this.economySelectedTrigger = economySelectedTrigger;
        this.balanceChangedTrigger = balanceChangedTrigger;
    }

    private EconomyAdapter getAdapter() {
        if (economyAdapter == null) {
            throw new IllegalStateException("Attempted to access the economy before it was initialized!");
        }

        return economyAdapter;
    }

    public void setEconomy(EconomyAdapter economyAdapter) {
        if (this.economyAdapter == null) {
            this.economyAdapter = economyAdapter;
            this.economySelectedTrigger.triggerSelected(this);
        }
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return getAdapter().getBalance(uuid, isPlayer);
    }

    @Override
    public String getFormattedBalance(UUID uuid, Boolean isPlayer) {
        return formatCurrency(getAdapter().getBalance(uuid, isPlayer));
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        double previousBalance = getBalance(uuid, isPlayer);
        if (configValues.isEnforceNonNegativeBalance()
            && amount < 0
            && previousBalance + amount < 0
        ) {
            return false;
        }
        boolean added = getAdapter().addToBalance(uuid, amount, isPlayer);
        if (added) {
            this.balanceChangedTrigger.triggerBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
        }
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (configValues.isEnforceNonNegativeBalance()
            && amount < 0
        ) {
            return false;
        }
        double oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = getAdapter().setBalance(uuid, amount, isPlayer);
        if (balanceSet) {
            this.balanceChangedTrigger.triggerBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        }
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        double previousBalance = getBalance(uuid, isPlayer);
        if (configValues.isEnforceNonNegativeBalance()
            && amount > 0
            && previousBalance - amount < 0
        ) {
            return false;
        }
        boolean taken = getAdapter().takeFromBalance(uuid, amount, isPlayer);
        if (taken) {
            this.balanceChangedTrigger.triggerBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
        }
        return taken;
    }

    @Override
    public String getCurrencyName(double amount) {
        return getAdapter().getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(double amount) {
        return getAdapter().getFormattedCurrency(amount);
    }

    @Override
    public String getEconomyModId() {
        return getAdapter().getId();
    }
}
