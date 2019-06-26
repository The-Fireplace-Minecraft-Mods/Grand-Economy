package the_fireplace.grandeconomy.econhandlers.fe;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.UserIdent;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class ForgeEssentialsEconHandler implements IEconHandler {
    @Override
    public long getBalance(UUID uuid) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).get();
    }

    @Override
    public void addToBalance(UUID uuid, long amount, boolean showMsg) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).add(amount);
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).withdraw(amount);
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, boolean showMsg) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).set(amount);
        return true;
    }

    @Override
    public String getCurrencyName(long amount) {
        return APIRegistry.economy.currency(amount);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)) != null;
    }

    @Override
    public void init() {

    }
}
