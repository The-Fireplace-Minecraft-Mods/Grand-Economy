package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.economy.Account;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApi {
    public static long getBalance(UUID uuid) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return 0;
        }
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount, boolean showMsg) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return;
        }
        account.addBalance(amount, showMsg);
    }

    public static boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
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

    public static String getCurrencyName(long amount) {
        if (amount == 1)
            return GrandEconomy.cfg.currencyNameSingular;
        return GrandEconomy.cfg.currencyNameMultiple;
    }
}
