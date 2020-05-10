package the_fireplace.grandeconomy.econhandlers.fe;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.UserIdent;
import com.forgeessentials.core.ForgeEssentials;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class ForgeEssentialsEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).get();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).add(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).withdraw((long) amount);
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).set((long) amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return APIRegistry.economy.currency((long) amount);
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return APIRegistry.economy.toString((long) amount);
    }

    @Override
    public String getId() {
        return ForgeEssentials.MODID;
    }

    @Override
    public void init() {

    }
}
