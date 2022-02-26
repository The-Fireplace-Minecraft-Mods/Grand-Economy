package dev.the_fireplace.grandeconomy.logintracker;

import dev.the_fireplace.grandeconomy.io.AccountData;
import dev.the_fireplace.grandeconomy.time.CurrentDay;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;

import java.util.UUID;

public final class LastLogin extends AccountData {

    private long lastLogin = CurrentDay.getCurrentDay();

    LastLogin(UUID uuid, SaveDataStateManager saveDataStateManager) {
        super(uuid, "login", saveDataStateManager);
        this.saveDataStateManager.initializeWithoutAutosave(this);
    }

    void setToToday() {
        this.lastLogin = CurrentDay.getCurrentDay();
    }

    long getDaysSinceLastLogin() {
        long today = CurrentDay.getCurrentDay();

        return today - this.lastLogin;
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
