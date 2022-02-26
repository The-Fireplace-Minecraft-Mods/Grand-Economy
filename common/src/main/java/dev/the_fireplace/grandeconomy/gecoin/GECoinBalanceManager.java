package dev.the_fireplace.grandeconomy.gecoin;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.domain.gecoin.BalanceTracker;
import dev.the_fireplace.lib.api.io.injectables.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class GECoinBalanceManager implements BalanceTracker {
	private final Map<UUID, GECoinBalance> accountBalances = new ConcurrentHashMap<>();

	private final SaveDataStateManager saveDataStateManager;
	private final SaveBasedStorageWriter saveBasedStorageWriter;
	private final ConfigValues configValues;

	@Inject
	public GECoinBalanceManager(SaveDataStateManager saveDataStateManager, SaveBasedStorageWriter saveBasedStorageWriter, ConfigValues configValues) {
		this.saveDataStateManager = saveDataStateManager;
		this.saveBasedStorageWriter = saveBasedStorageWriter;
		this.configValues = configValues;
	}

	private GECoinBalance getOrCreateBalance(UUID accountId) {
	    return accountBalances.computeIfAbsent(accountId, (uuid -> new GECoinBalance(uuid, saveDataStateManager, configValues)));
	}

	@Override
	public double getBalance(UUID accountId) {
		return getOrCreateBalance(accountId).getBalance();
	}

	@Override
	public void setBalance(UUID accountId, double balance) {
		getOrCreateBalance(accountId).setBalance(balance);
	}

	@Override
	public void addToBalance(UUID accountId, double amount) {
		getOrCreateBalance(accountId).addBalance(amount);
	}

	@Override
	public boolean hasBalance(UUID playerId) {
		return accountBalances.containsKey(playerId);
	}

	@Override
	public void deleteBalance(UUID playerId) {
		GECoinBalance account = accountBalances.remove(playerId);
		if (account != null) {
			saveDataStateManager.tearDown(account);
			saveBasedStorageWriter.delete(account);
		}
	}
}
