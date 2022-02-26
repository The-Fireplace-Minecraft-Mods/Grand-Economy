package dev.the_fireplace.grandeconomy.logintracker;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.domain.tracker.LoginTracker;
import dev.the_fireplace.lib.api.io.injectables.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class PlayerLoginTracker implements LoginTracker {
    private final Economy economy;
    private final SaveBasedStorageWriter saveBasedStorageWriter;
    private final SaveDataStateManager saveDataStateManager;
    private final ConfigValues configValues;

    private final Map<UUID, LastLogin> loginTrackers = new ConcurrentHashMap<>();

    @Inject
    public PlayerLoginTracker(
        Economy economy,
        SaveBasedStorageWriter saveBasedStorageWriter,
        SaveDataStateManager saveDataStateManager,
        ConfigValues configValues
    ) {
        this.economy = economy;
        this.saveBasedStorageWriter = saveBasedStorageWriter;
        this.saveDataStateManager = saveDataStateManager;
        this.configValues = configValues;
    }

    @Override
    public void addLogin(UUID playerId) {
        LastLogin lastLogin = loginTrackers.computeIfAbsent(playerId, (uuid) -> new LastLogin(uuid, saveDataStateManager));

        long daysSinceLastLogin = lastLogin.getDaysSinceLastLogin();
        lastLogin.setToToday();

        if (daysSinceLastLogin == 0) {
            return;
        }

        //TODO save???

        distributeBasicIncome(playerId, daysSinceLastLogin);
    }

    @Override
    public long getDaysSinceLastLogin(UUID playerId) {
        return hasLoggedInBefore(playerId)
            ? loginTrackers.get(playerId).getDaysSinceLastLogin()
            : Long.MIN_VALUE;
    }

    @Override
    public boolean hasLoggedInBefore(UUID playerId) {
        return loginTrackers.containsKey(playerId);
    }

    @Override
    public void deleteLoginData(UUID playerId) {
        LastLogin account = loginTrackers.remove(playerId);
        if (account != null) {
            saveDataStateManager.tearDown(account);
            saveBasedStorageWriter.delete(account);
        }
    }

    private void distributeBasicIncome(UUID playerId, long days) {
        if (configValues.isBasicIncome()) {
            if (days > configValues.getMaxIncomeSavingsDays()) {
                days = configValues.getMaxIncomeSavingsDays();
            }
            economy.addToBalance(playerId, days * configValues.getBasicIncomeAmount(), true);
        }
    }
}
