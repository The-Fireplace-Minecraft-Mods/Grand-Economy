package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.Config;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.economy.Account;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApi {
    public static long getBalance(UUID uuid) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.logger.warn("Account for "+uuid+" was null");
            return 0;
        }
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount, boolean showMsg) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.logger.warn("Account for "+uuid+" was null");
            return;
        }
        account.addBalance(amount, showMsg);
    }

    public static boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.logger.warn("Account for "+uuid+" was null");
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount, showMsg);
        return true;
    }

    public static String getCurrencyName(long amount) {
        if (amount == 1)
            return Config.currencyNameSingular;
        return Config.currencyNameMultiple;
    }
}
