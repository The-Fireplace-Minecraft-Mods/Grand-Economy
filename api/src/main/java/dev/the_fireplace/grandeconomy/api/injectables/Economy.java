package dev.the_fireplace.grandeconomy.api.injectables;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * The accessor for economy functions.
 * This will always use the currently selected adapter, or throw an exception if one has not yet been selected.
 * Please wait for the Economy Selected event before calling any economy functions.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Economy
{
    /**
     * Check the account's balance
     *
     * @param uuid The account to check the balance of
     * @return The balance
     */
    double getBalance(UUID uuid, @Nullable Boolean isPlayer);

    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance, formatted with the currency name
     */
    String getFormattedBalance(UUID uuid, @Nullable Boolean isPlayer);

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
    boolean addToBalance(UUID uuid, double amount, @Nullable Boolean isPlayer);

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
    boolean setBalance(UUID uuid, double amount, @Nullable Boolean isPlayer);

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
    boolean takeFromBalance(UUID uuid, double amount, @Nullable Boolean isPlayer);

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    String getCurrencyName(double amount);

    /**
     * Gets the currency amount with the currency name attached
     * @param amount
     * The currency amount
     * @return
     * The currency amount with the name attached
     */
    String formatCurrency(double amount);

    /**
     * Get the modid of the economy Grand Economy is using for currency.
     */
    String getEconomyModId();
}
