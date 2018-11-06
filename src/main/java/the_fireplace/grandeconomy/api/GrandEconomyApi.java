package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.economy.Account;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class GrandEconomyApi {
    public static long getBalance(UUID uuid) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        account.addBalance(amount);
    }

    public static void takeFromBalance(UUID uuid, long amount) throws InsufficientCreditException, NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        if (account.getBalance() < amount) throw new InsufficientCreditException();
        account.addBalance(-amount);
    }

    public static void takeFromBalanceNegative(UUID uuid, long amount) throws NoSuchAccountException {
        addToBalance(uuid, -amount);
    }

    @Deprecated
    public static String getCurrencyNameSingular() {
        return GrandEconomy.settings.getCurrencyNameSingular();
    }

    @Deprecated
    public static String getCurrencyNameMultiple() {
        return GrandEconomy.settings.getCurrencyNameMultiple();
    }

    public static String getCurrencyName(long amount) {
        if (amount == 1) return GrandEconomy.settings.getCurrencyNameSingular();
        return GrandEconomy.settings.getCurrencyNameMultiple();
    }
}
