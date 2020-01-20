package the_fireplace.grandeconomy.econhandlers.ge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.compat.sponge.ISpongeCompat;
import the_fireplace.grandeconomy.compat.sponge.RegisterSpongeEconomy;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class GrandEconomyEconHandler implements IEconHandler {
    @Override
    public long getBalance(UUID uuid, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return 0;
        }
        account.update();
        return account.getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if(amount < 0)
            return false;

        account.setBalance(amount);
        return true;
    }

    @Override
    public String getCurrencyName(long amount) {
        if (amount == 1)
            return GrandEconomy.cfg.currencyNameSingular;
        return GrandEconomy.cfg.currencyNameMultiple;
    }

    @Override
    public String toString(long amount) {
        return String.valueOf(amount) + ' ' + getCurrencyName(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return Account.get(uuid) != null;
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        try {
            Objects.requireNonNull(Account.get(uuid)).writeIfChanged();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    @Override
    public String getId() {
        return GrandEconomy.MODID;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        if(Loader.isModLoaded("spongeapi")) {
            ISpongeCompat compat = new RegisterSpongeEconomy();
            compat.register();
        }
    }
}
