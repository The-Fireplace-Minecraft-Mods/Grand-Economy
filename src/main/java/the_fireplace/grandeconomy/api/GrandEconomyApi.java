package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.economy.Account;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class GrandEconomyApi {
    public static long getBalance(UUID uuid) {
        Account account = Account.get(uuid);
        if (account == null){
            //TODO: Log error
            return 0;
        }
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount) {
        Account account = Account.get(uuid);
        if(account == null) {
            //TODO: Log error
            return;
        }
        account.addBalance(amount);
    }

    public static boolean takeFromBalance(UUID uuid, long amount) {
        Account account = Account.get(uuid);
        if (account == null){
            //TODO: Log error
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        return true;
    }

    public static String getCurrencyName(long amount) {
        if (amount == 1)
            return GrandEconomy.settings.getCurrencyNameSingular();
        return GrandEconomy.settings.getCurrencyNameMultiple();
    }
}
