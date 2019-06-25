package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.GrandEconomy;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApi {
    public static long getBalance(UUID uuid) {
        return GrandEconomy.economy.getBalance(uuid);
    }

    public static void addToBalance(UUID uuid, long amount, boolean showMsg) {
        GrandEconomy.economy.addToBalance(uuid, amount, showMsg);
    }

    public static boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        return GrandEconomy.economy.takeFromBalance(uuid, amount, showMsg);
    }

    public static String getCurrencyName(long amount) {
        return GrandEconomy.economy.getCurrencyName(amount);
    }
}
