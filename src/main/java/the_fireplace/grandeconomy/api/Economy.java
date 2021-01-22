package the_fireplace.grandeconomy.api;

import java.util.UUID;

public interface Economy {
    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @param isPlayer
     * If the account is known to be a player, true. If it is known not to be a player, false. null otherwise.
     * @return
     * The balance
     */
    double getBalance(UUID uuid, Boolean isPlayer);

    /**
     * Add to the account's balance
     * @param uuid
     * The account to increase the balance of
     * @param amount
     * The amount to increase the account balance by
     * @param isPlayer
     * If the account is known to be a player, true. If it is known not to be a player, false. null otherwise.
     * @return
     * Whether the amount was successfully added or not
     */
    boolean addToBalance(UUID uuid, double amount, Boolean isPlayer);

    /**
     * Take from the account's balance
     * @param uuid
     * The account to decrease the balance of
     * @param amount
     * The amount to decrease the account balance by
     * @param isPlayer
     * If the account is known to be a player, true. If it is known not to be a player, false. null otherwise.
     * @return
     * Whether the amount was successfully taken or not
     */
    boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer);

    /**
     * Sets the account's balance
     * @param uuid
     * The account to set the balance of
     * @param amount
     * The amount to set the account balance to
     * @param isPlayer
     * If the account is known to be a player, true. If it is known not to be a player, false. null otherwise.
     * @return
     * Whether the balance was successfully set or not
     */
    boolean setBalance(UUID uuid, double amount, Boolean isPlayer);

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    String getCurrencyName(double amount);

    /**
     * Gets the currency amount with the currency name/symbol attached
     * @param amount
     * The currency amount
     * @return
     * The currency amount with the name attached
     */
    String getFormattedCurrency(double amount);

    /**
     * Get the modid of the economy mod this is using.
     */
    String getId();

    void init();
}
