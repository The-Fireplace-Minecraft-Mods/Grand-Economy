package dev.the_fireplace.grandeconomy.io;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.SaveData;

import java.util.UUID;

public abstract class AccountData implements SaveData {
    protected static final short SAVE_INTERVAL_IN_MINUTES = 5;
    private final UUID accountId;
    private final String dataTableName;
    protected final SaveDataStateManager saveDataStateManager;

    protected AccountData(UUID accountId, String dataTableName, SaveDataStateManager saveDataStateManager) {
        super();
        this.accountId = accountId;
        this.dataTableName = dataTableName;
        this.saveDataStateManager = saveDataStateManager;
    }

    @Override
    public String getDatabase() {
        return GrandEconomyConstants.MODID;
    }

    @Override
    public String getTable() {
        return dataTableName;
    }

    @Override
    public String getId() {
        return accountId.toString();
    }

    public UUID getAccountId() {
        return accountId;
    }

    protected void markChanged() {
        saveDataStateManager.markChanged(this);
    }
}
