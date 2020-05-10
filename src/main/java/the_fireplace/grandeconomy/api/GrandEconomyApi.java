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
    public static long getBalance(UUID uuid, Boolean isPlayer) {
        return GrandEconomy.getEconomy().getBalance(uuid, isPlayer);
    }

    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance, formatted with the currency name
     */
    public static String getFormattedBalance(UUID uuid, Boolean isPlayer) {
        return getFormattedCurrency(GrandEconomy.getEconomy().getBalance(uuid, isPlayer));
    }

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
    public static boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean added = GrandEconomy.getEconomy().addToBalance(uuid, amount, isPlayer);
        if(added)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return added;
    }

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
    public static boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = GrandEconomy.getEconomy().setBalance(uuid, amount, isPlayer);
        if(balanceSet)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return balanceSet;
    }

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
    public static boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean taken = GrandEconomy.getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if(taken)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return taken;
    }

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    public static String getCurrencyName(double amount) {
        return GrandEconomy.getEconomy().getCurrencyName(amount);
    }

    /**
     * Gets the currency amount with the currency name attached
     * @param amount
     * The currency amount
     * @return
     * The currency amount with the name attached
     */
    public static String getFormattedCurrency(double amount) {
        return GrandEconomy.getEconomy().getFormattedCurrency(amount);
    }

    /**
     * Get the modid of the economy Grand Economy is using for currency.
     */
    public static String getEconomyModId() {
        return GrandEconomy.getEconomy().getId();
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
