package the_fireplace.grandeconomy.api;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.event.BalanceChangeEvent;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

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
        long oldAmount = getBalance(uuid);
        boolean added = GrandEconomy.economy.addToBalance(uuid, amount, showMsg);
        if(added)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid), uuid));
        return added;
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
        long oldAmount = getBalance(uuid);
        boolean balanceSet = GrandEconomy.economy.setBalance(uuid, amount, showMsg);
        if(balanceSet)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid), uuid));
        return balanceSet;
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
        long oldAmount = getBalance(uuid);
        boolean taken = GrandEconomy.economy.takeFromBalance(uuid, amount, showMsg);
        if(taken)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid), uuid));
        return taken;
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

    /**
     * Get the modid of the economy Grand Economy is using for currency.
     */
    public static String getEconomyModId() {
        return GrandEconomy.economy.getId();
    }

    /**
     * Register an economy handler for your mod. This will allow setting the "economy bridge" config option to forModid or one of the aliases to have GE use your economy handler.
     * This must be done BEFORE {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}.
     * @param handler
     * Your economy handler instance
     * @param forModid
     * The modid the handler is for.
     * @param aliases
     * Aliases that can also be used in the config to use your currency. The method still returns true if any of these are already taken, as long as the modid isn't.
     * @return false if registering the handler failed (currently the only reason is if another handler is registered with the given modid) and true otherwise
     */
    public static boolean registerEconomyHandler(IEconHandler handler, String forModid, String... aliases) {
        return GrandEconomy.registerEconHandler(handler, forModid, aliases);
    }
}
