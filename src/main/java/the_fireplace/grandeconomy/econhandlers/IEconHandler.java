package the_fireplace.grandeconomy.econhandlers;

import java.util.UUID;

public interface IEconHandler {
    long getBalance(UUID uuid);

    void addToBalance(UUID uuid, long amount, boolean showMsg);

    boolean takeFromBalance(UUID uuid, long amount, boolean showMsg);

    boolean setBalance(UUID uuid, long amount, boolean showMsg);

    String getCurrencyName(long amount);

    boolean hasAccount(UUID uuid);

    void init();
}
