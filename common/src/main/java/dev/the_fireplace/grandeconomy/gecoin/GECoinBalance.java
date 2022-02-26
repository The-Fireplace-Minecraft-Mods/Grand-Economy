package dev.the_fireplace.grandeconomy.gecoin;

import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.io.AccountData;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;

import java.util.UUID;

public final class GECoinBalance extends AccountData {

    private final ConfigValues configValues;
    private double balance;

    GECoinBalance(UUID uuid, SaveDataStateManager saveDataStateManager, ConfigValues configValues) {
        super(uuid, "account", saveDataStateManager);
        this.configValues = configValues;
        this.saveDataStateManager.initializeWithAutosave(this, SAVE_INTERVAL_IN_MINUTES);
    }

    double getBalance() {
        return balance;
    }

    void setBalance(double v) {
        if (balance != v) {
            balance = v;
            markChanged();
        }
    }

    void addBalance(double v) {
        setBalance(balance + v);
    }

    @Override
    public void readFrom(StorageReadBuffer reader) {
        balance = reader.readDouble("balance", configValues.getStartBalance());
    }

    @Override
    public void writeTo(StorageWriteBuffer writer) {
        writer.writeDouble("balance", balance);
    }
}
