package dev.the_fireplace.grandeconomy.io;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.lib.api.storage.lazy.ThreadsafeLazySavable;

import java.util.UUID;

public abstract class AccountData extends ThreadsafeLazySavable {
    protected static final int SAVE_INTERVAL_IN_MINUTES = 5;
    private final UUID accountId;
    private final String dataTableName;
    protected AccountData(UUID accountId, String dataTableName) {
        super();
        this.accountId = accountId;
        this.dataTableName = dataTableName;
    }

    @Override
    public String getDatabase() {
        return GrandEconomy.MODID;
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
}
