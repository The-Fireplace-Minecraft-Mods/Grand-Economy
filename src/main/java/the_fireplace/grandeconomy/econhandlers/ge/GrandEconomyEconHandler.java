package the_fireplace.grandeconomy.econhandlers.ge;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class GrandEconomyEconHandler implements IEconHandler {
    public long getBalance(UUID uuid) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return 0;
        }
        account.update();
        return account.getBalance();
    }

    public void addToBalance(UUID uuid, long amount, boolean showMsg) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return;
        }
        account.addBalance(amount, showMsg);
    }

    public boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount, showMsg);
        return true;
    }

    public String getCurrencyName(long amount) {
        if (amount == 1)
            return GrandEconomy.cfg.currencyNameSingular;
        return GrandEconomy.cfg.currencyNameMultiple;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
