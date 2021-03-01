package dev.the_fireplace.grandeconomy.impl;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.CurrencyAPI;
import dev.the_fireplace.grandeconomy.api.Economy;
import dev.the_fireplace.grandeconomy.api.event.BalanceChangeEvent;

import java.util.UUID;

public final class CurrencyManager implements CurrencyAPI {
    @Deprecated
    public static final CurrencyAPI INSTANCE = new CurrencyManager();
    
    private final Economy economy;

    private CurrencyManager() {
        this.economy = GrandEconomy.getEconomy();
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return economy.getBalance(uuid, isPlayer);
    }

    @Override
    public String getFormattedBalance(UUID uuid, Boolean isPlayer) {
        return formatCurrency(economy.getBalance(uuid, isPlayer));
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean added = economy.addToBalance(uuid, amount, isPlayer);
        if (added) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        }
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = economy.setBalance(uuid, amount, isPlayer);
        if (balanceSet) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        }
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean taken = economy.takeFromBalance(uuid, amount, isPlayer);
        if (taken) {
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        }
        return taken;
    }

    @Override
    public String getCurrencyName(double amount) {
        return economy.getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(double amount) {
        return economy.getFormattedCurrency(amount);
    }

    @Override
    public String getEconomyModId() {
        return economy.getId();
    }
}
