package dev.the_fireplace.grandeconomy.gecoin;

import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.io.AccountData;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Defaultable;

import java.util.UUID;

public final class GECoinBalance extends AccountData implements Defaultable {

    private final ConfigValues configValues;
    private double balance;
    private boolean isPlayer;

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

    private boolean calculateIsPlayer() {
        // Why does it matter???
        //TODO return FireplaceLib.getServer().getPlayerManager().getPlayer(getId()) != null;
        return false;
    }

    @Override
    public void readFrom(StorageReadBuffer reader) {
        balance = reader.readDouble("balance", configValues.getStartBalance());
        isPlayer = reader.readBool("isPlayer", calculateIsPlayer());
    }

    @Override
    public void writeTo(StorageWriteBuffer writer) {
        writer.writeDouble("balance", balance);
        writer.writeBool("isPlayer", isPlayer);
    }

    @Override
    public boolean isDefault() {
        return balance == configValues.getStartBalance() && !isPlayer;
    }
}
