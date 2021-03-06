package dev.the_fireplace.grandeconomy;

import dev.the_fireplace.grandeconomy.api.Economy;
import dev.the_fireplace.grandeconomy.api.event.BalanceChangeEvent;
import dev.the_fireplace.grandeconomy.api.event.EconomySelectedEvent;
import dev.the_fireplace.grandeconomy.config.ModConfig;

import java.util.UUID;

class BoundedEconomyWrapper implements Economy {
	private Economy economy;

	void setEconomy(Economy economy) {
		this.economy = economy;
		EconomySelectedEvent.EVENT.invoker().onEconomySelected(economy);
	}

	@Override
	public double getBalance(UUID uuid, Boolean isPlayer) {
		return economy.getBalance(uuid, isPlayer);
	}

	@Override
	public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
		double previousBalance = getBalance(uuid, isPlayer);
        if (ModConfig.getData().isEnforceNonNegativeBalance() && amount < 0) {
			if (previousBalance + amount < 0)
				return false;
		}
		boolean added = economy.addToBalance(uuid, amount, isPlayer);
		if (added) {
			BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
		}
		return added;
	}

	@Override
	public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
		double previousBalance = getBalance(uuid, isPlayer);
        if (ModConfig.getData().isEnforceNonNegativeBalance() && amount > 0) {
			if (previousBalance - amount < 0)
				return false;
		}
		boolean taken = economy.takeFromBalance(uuid, amount, isPlayer);
		if (taken) {
			BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
		}
		return taken;
	}

	@Override
	public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (ModConfig.getData().isEnforceNonNegativeBalance() && amount < 0)
			return false;
		double previousBalance = getBalance(uuid, isPlayer);
		boolean balanceSet = economy.setBalance(uuid, amount, isPlayer);
		if (balanceSet) {
			BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, previousBalance, getBalance(uuid, isPlayer));
		}
		return balanceSet;
	}

	@Override
	public String getCurrencyName(double amount) {
		return economy.getCurrencyName(amount);
	}

	@Override
	public String getFormattedCurrency(double amount) {
		return economy.getFormattedCurrency(amount);
	}

	@Override
	public String getId() {
		return economy.getId();
	}

	@Override
	public void init() {
		economy.init();
	}
}
