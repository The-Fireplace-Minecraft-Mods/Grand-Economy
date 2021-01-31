package the_fireplace.grandeconomy;

import the_fireplace.grandeconomy.api.Economy;

import java.util.UUID;

class BoundedEconomyWrapper implements Economy {
	Economy economy;

	void setEconomy(Economy economy) {
		this.economy = economy;
	}

	@Override
	public double getBalance(UUID uuid, Boolean isPlayer) {
		return economy.getBalance(uuid, isPlayer);
	}

	@Override
	public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
		if (GrandEconomy.config.enforceNonNegativeBalance && amount < 0) {
			if (getBalance(uuid, isPlayer) + amount < 0)
				return false;
		}
		return economy.addToBalance(uuid, amount, isPlayer);
	}

	@Override
	public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
		if (GrandEconomy.config.enforceNonNegativeBalance && amount > 0) {
			if (getBalance(uuid, isPlayer) - amount < 0)
				return false;
		}
		return economy.takeFromBalance(uuid, amount, isPlayer);
	}

	@Override
	public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
		if (GrandEconomy.config.enforceNonNegativeBalance && amount < 0)
			return false;
		return economy.setBalance(uuid, amount, isPlayer);
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
