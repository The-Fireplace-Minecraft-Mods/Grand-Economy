package the_fireplace.grandeconomy.econhandlers.fe;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.UserIdent;
import com.forgeessentials.core.ForgeEssentials;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class ForgeEssentialsEconHandler implements IEconHandler {
    @Override
    public long getBalance(UUID uuid, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).get();
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).add(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).withdraw(amount);
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).set(amount);
        return true;
    }

    @Override
    public String getCurrencyName(long amount) {
        return APIRegistry.economy.currency(amount);
    }

    @Override
    public String toString(long amount) {
        return APIRegistry.economy.toString(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)) != null;
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        return null;
    }

    @Override
    public String getId() {
        return ForgeEssentials.MODID;
    }

    @Override
    public void init() {

    }
}
