package the_fireplace.grandeconomy.econhandlers;

import java.util.UUID;

public interface IEconHandler {
    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance
     */
    long getBalance(UUID uuid);

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
    boolean addToBalance(UUID uuid, long amount, boolean showMsg);

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
    boolean takeFromBalance(UUID uuid, long amount, boolean showMsg);

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
    boolean setBalance(UUID uuid, long amount, boolean showMsg);

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    String getCurrencyName(long amount);

    /**
     * Try to make sure an account exists.
     * @param uuid
     * the account to check
     * @return
     * True if it exists, false otherwise.
     */
    boolean ensureAccountExists(UUID uuid);

    /**
     * Forcibly saves the account.
     * @param uuid
     * The account to save
     * @return
     * true if saved, false if not saved, or null if not implemented
     */
    Boolean forceSave(UUID uuid);

    /**
     * Get the modid of the economy mod this is using.
     */
    String getId();

    void init();
}
