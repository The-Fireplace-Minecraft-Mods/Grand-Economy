package dev.the_fireplace.grandeconomy.impl;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.BalanceChangeEvent;
import dev.the_fireplace.grandeconomy.api.events.EconomySelectedEvent;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.api.interfaces.Economy;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@SuppressWarnings("LawOfDemeter")
@Implementation
@Singleton
public final class CurrencyManager implements CurrencyAPI {
    
    private final ConfigValues configValues;
    private Economy economy = null;
    
    @Inject
    public CurrencyManager(ConfigValues configValues) {
        this.configValues = configValues;
    }

    private Economy getEconomy() {
        if (economy == null) {
            throw new IllegalStateException("Attempted to access the economy before it was initialized!");
        }

        return economy;
    }

    public void setEconomy(Economy economy) {
        if (this.economy == null) {
            this.economy = economy;
            EconomySelectedEvent.EVENT.invoker().onEconomySelected(economy);
        }
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return getEconomy().getBalance(uuid, isPlayer);
    }

    @Override
    public String getFormattedBalance(UUID uuid, Boolean isPlayer) {
        return formatCurrency(getEconomy().getBalance(uuid, isPlayer));
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
        boolean added = getEconomy().addToBalance(uuid, amount, isPlayer);
        if (added) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
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
        boolean balanceSet = getEconomy().setBalance(uuid, amount, isPlayer);
        if (balanceSet) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
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
        boolean taken = getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if (taken) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
        }
        return taken;
    }

    @Override
    public String getCurrencyName(double amount) {
        return getEconomy().getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(double amount) {
        return getEconomy().getFormattedCurrency(amount);
    }

    @Override
    public String getEconomyModId() {
        return getEconomy().getId();
    }
}
