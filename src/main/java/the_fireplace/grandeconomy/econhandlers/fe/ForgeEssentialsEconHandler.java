package the_fireplace.grandeconomy.econhandlers.fe;

import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class ForgeEssentialsEconHandler implements IEconHandler {
    @Override
    public long getBalance(UUID uuid) {
        return 0;
    }

    @Override
    public void addToBalance(UUID uuid, long amount, boolean showMsg) {

    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        return false;
    }

    @Override
    public String getCurrencyName(long amount) {
        return null;
    }

    @Override
    public void init() {

    }
}
