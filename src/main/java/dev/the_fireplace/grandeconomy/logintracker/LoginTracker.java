package dev.the_fireplace.grandeconomy.logintracker;

import dev.the_fireplace.grandeconomy.api.CurrencyAPI;
import dev.the_fireplace.grandeconomy.config.ModConfig;
import dev.the_fireplace.grandeconomy.io.AccountData;
import dev.the_fireplace.grandeconomy.time.CurrentDay;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;
import dev.the_fireplace.lib.api.storage.lazy.LazySavableInitializer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LoginTracker extends AccountData {
    private static final Map<UUID, LoginTracker> PLAYER_TIME_TRACKER_INSTANCES = new ConcurrentHashMap<>();

    public static LoginTracker get(UUID accountId) {
        return PLAYER_TIME_TRACKER_INSTANCES.computeIfAbsent(accountId, LoginTracker::create);
    }

    private static LoginTracker create(UUID uuid) {
        return LazySavableInitializer.lazyInitialize(new LoginTracker(uuid), SAVE_INTERVAL_IN_MINUTES);
    }

    public static void delete(UUID accountId) {
        LoginTracker account = PLAYER_TIME_TRACKER_INSTANCES.remove(accountId);
        if (account != null) {
            LazySavableInitializer.tearDown(account, SAVE_INTERVAL_IN_MINUTES);
            SaveBasedStorageWriter.getInstance().delete(account);
        }
    }

    private long lastLogin = CurrentDay.getCurrentDay();
    private final CurrencyAPI currencyAPI = CurrencyAPI.getInstance();

    private LoginTracker(UUID uuid) {
        super(uuid, "login");
        markChanged();
    }

    public void addLogin() {
        long now = CurrentDay.getCurrentDay();
        long daysSinceLastLogin = now - this.lastLogin;
        this.lastLogin = now;

        if (daysSinceLastLogin == 0) {
            return;
        }

        distributeBasicIncome(daysSinceLastLogin);
    }

    private void distributeBasicIncome(long days) {
        if (ModConfig.getData().isBasicIncome()) {
            if (days > ModConfig.getData().getMaxIncomeSavingsDays())
                days = ModConfig.getData().getMaxIncomeSavingsDays();
            currencyAPI.addToBalance(getAccountId(), days * ModConfig.getData().getBasicIncomeAmount(), true);
        }
    }

    @Override
    public void readFrom(StorageReadBuffer reader) {
        lastLogin = reader.readLong("lastLogin", lastLogin);
    }

    @Override
    public void writeTo(StorageWriteBuffer writer) {
        writer.writeLong("lastLogin", lastLogin);
    }
}
