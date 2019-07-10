package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.GrandEconomy;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApi {
    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance
     */
    public static long getBalance(UUID uuid) {
        return GrandEconomy.economy.getBalance(uuid);
    }

    /**
     * Add to the account's balance
     * @param uuid
     * The account to increase the balance of
     * @param amount
     * The amount to increase the account balance by
     * @param showMsg
     * Whether or not a message should be shown to the player whose balance was updated. The effectiveness of this is economy-specific.
     * @return
     * Whether the amount was successfully added or not
     */
    public static boolean addToBalance(UUID uuid, long amount, boolean showMsg) {
        return GrandEconomy.economy.addToBalance(uuid, amount, showMsg);
    }

    /**
     * Sets the account's balance
     * @param uuid
     * The account to set the balance of
     * @param amount
     * The amount to set the account balance to
     * @param showMsg
     * Whether or not a message should be shown to the player whose balance was updated. The effectiveness of this is economy-specific.
     * @return
     * Whether the balance was successfully set or not
     */
    public static boolean setBalance(UUID uuid, long amount, boolean showMsg) {
        return GrandEconomy.economy.setBalance(uuid, amount, showMsg);
    }

    /**
     * Take from the account's balance
     * @param uuid
     * The account to decrease the balance of
     * @param amount
     * The amount to decrease the account balance by
     * @param showMsg
     * Whether or not a message should be shown to the player whose balance was updated. The effectiveness of this is economy-specific.
     * @return
     * Whether the amount was successfully taken or not
     */
    public static boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        return GrandEconomy.economy.takeFromBalance(uuid, amount, showMsg);
    }

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    public static String getCurrencyName(long amount) {
        return GrandEconomy.economy.getCurrencyName(amount);
    }

    /**
     * Try to make sure an account exists.
     * @param uuid
     * the account to check
     * @return
     * True if it exists, false otherwise.
     */
    public static boolean ensureAccountExists(UUID uuid) {
        return GrandEconomy.economy.ensureAccountExists(uuid);
    }

    /**
     * Forcibly saves the account.
     * @param uuid
     * The account to save
     * @return
     * true if saved, false if not saved, or null if not implemented
     */
    public static Boolean forceSave(UUID uuid) {
        return GrandEconomy.economy.forceSave(uuid);
    }
}
