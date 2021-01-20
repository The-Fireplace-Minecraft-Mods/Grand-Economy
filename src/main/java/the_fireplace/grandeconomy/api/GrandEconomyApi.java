package the_fireplace.grandeconomy.api;

import org.jetbrains.annotations.Nullable;
import the_fireplace.grandeconomy.requesthandler.GrandEconomyApiImpl;
import the_fireplace.grandeconomy.requesthandler.IGrandEconomyApi;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"UnusedReturnValue", "unused", "RedundantSuppression"})
public final class GrandEconomyApi {
    private static final IGrandEconomyApi API = new GrandEconomyApiImpl();//TODO deal with this

    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance
     */
    public static double getBalance(UUID uuid, @Nullable Boolean isPlayer) {
        return API.getBalance(uuid, isPlayer);
    }

    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance, formatted with the currency name
     */
    public static String getBalanceFormatted(UUID uuid, Boolean isPlayer) {
        return API.getBalanceFormatted(uuid, isPlayer);
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
        return API.addToBalance(uuid, amount, isPlayer);
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
    public static boolean setBalance(UUID uuid, double amount, @Nullable Boolean isPlayer) {
        return API.setBalance(uuid, amount, isPlayer);
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
        return API.takeFromBalance(uuid, amount, isPlayer);
    }

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    public static String getCurrencyName(double amount) {
        return API.getCurrencyName(amount);
    }

    /**
     * Gets the currency amount with the currency name attached
     * @param amount
     * The currency amount
     * @return
     * The currency amount with the name attached
     */
    public static String formatCurrency(double amount) {
        return API.formatCurrency(amount);
    }

    /**
     * Get the modid of the economy Grand Economy is using for currency.
     */
    public static String getEconomyModId() {
        return API.getEconomyModId();
    }

    private static final Map<String, EconomyHandler> econHandlers = new ConcurrentHashMap<>();

    /**
     * Check if an economy handler exists for the given modid or alias
     * @param key
     * A modid or alias
     */
    public static boolean hasEconomyHandler(String key) {
        return econHandlers.containsKey(key);
    }

    public static EconomyHandler getEconomyHandler(String key) {
        return econHandlers.get(key);
    }

    public static Collection<String> getEconomyHandlers() {
        return econHandlers.keySet();
    }

    /**
     * Register an economy handler for your mod. This will allow setting the "economy bridge" config option to forModid or one of the aliases to have GE use your economy handler.
     * This must be done BEFORE {@link net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents#SERVER_STARTED}.
     * @param handler
     * Your economy handler instance
     * @param modid
     * The modid the handler is for.
     * @param aliases
     * Aliases that can also be used in the config to use your currency. The method still returns true if any of these are already taken, as long as the modid isn't.
     * @return false if registering the handler failed (currently the only reason is if another handler is registered with the given modid) and true otherwise
     */
    public static boolean registerEconomyHandler(EconomyHandler handler, String modid, String... aliases) {
        if(econHandlers.containsKey(modid))
            return false;
        econHandlers.put(modid, handler);
        for(String alias: aliases)
            econHandlers.putIfAbsent(alias, handler);
        return true;
    }
}
