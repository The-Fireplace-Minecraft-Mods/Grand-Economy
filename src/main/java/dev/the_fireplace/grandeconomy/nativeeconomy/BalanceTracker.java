package dev.the_fireplace.grandeconomy.nativeeconomy;

import dev.the_fireplace.grandeconomy.config.ModConfig;
import dev.the_fireplace.grandeconomy.io.AccountData;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;
import dev.the_fireplace.lib.api.storage.lazy.Defaultable;
import dev.the_fireplace.lib.api.storage.lazy.LazySavableInitializer;
import dev.the_fireplace.lib.impl.FireplaceLib;

import java.util.UUID;

public final class BalanceTracker extends AccountData implements Defaultable {
    private double balance = ModConfig.getData().getStartBalance();
    private boolean isPlayer = calculateIsPlayer();

    private BalanceTracker(UUID uuid) {
        super(uuid, "account");
    }

    public static BalanceTracker create(UUID uuid) {
        return LazySavableInitializer.lazyInitialize(new BalanceTracker(uuid), SAVE_INTERVAL_IN_MINUTES);
    }

    double getBalance() {
        return balance;
    }

    void setBalance(double v) {
        if(balance != v) {
            balance = v;
            markChanged();
        }
    }

    void addBalance(double v) {
        setBalance(balance + v);
    }

    private boolean calculateIsPlayer() {
        return FireplaceLib.getServer().getPlayerManager().getPlayer(getId()) != null;
    }

    @Override
    public void readFrom(StorageReadBuffer reader) {
        balance = reader.readDouble("balance", balance);
        isPlayer = reader.readBool("isPlayer", calculateIsPlayer());
    }

    @Override
    public void writeTo(StorageWriteBuffer writer) {
        writer.writeDouble("balance", balance);
        writer.writeBool("isPlayer", isPlayer);
    }

    @Override
    public boolean isDefault() {
        return balance == ModConfig.getData().getStartBalance() && !isPlayer;
    }

    void delete() {
        LazySavableInitializer.tearDown(this, SAVE_INTERVAL_IN_MINUTES);
        SaveBasedStorageWriter.getInstance().delete(this);
    }
}
