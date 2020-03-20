package the_fireplace.grandeconomy.api;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class GrandEconomyApi {
    public static final String MODID = "grandeconomy";

    private static IGrandEconomyApi API = null;
    static void setAPI(IGrandEconomyApi api) {
        if(API == null)
            API = api;
    }
    public static IGrandEconomyApi getAPI() {
        return API;
    }

    /**
     * Check the account's balance
     * @param uuid
     * The account to check the balance of
     * @return
     * The balance
     */
    public static long getBalance(UUID uuid, Boolean isPlayer) {
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
    public static boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
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
    public static boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
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
    public static boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        return API.takeFromBalance(uuid, amount, isPlayer);
    }

    /**
     * Gets the name of the currency for the given amount.
     * @param amount
     * The amount to check. This is typically used to determine whether singular or plural.
     * @return
     * The currency name
     */
    public static String getCurrencyName(long amount) {
        return API.getCurrencyName(amount);
    }

    /**
     * Gets the currency amount with the currency name attached
     * @param amount
     * The currency amount
     * @return
     * The currency amount with the name attached
     */
    public static String formatCurrency(long amount) {
        return API.formatCurrency(amount);
    }

    /**
     * Try to make sure an account exists.
     * @param uuid
     * the account to check
     * @param isPlayer
     * If the account is known to be a player, true. If it is known not to be a player, false. null otherwise.
     * @return
     * True if it exists, false otherwise.
     */
    public static boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return API.ensureAccountExists(uuid, isPlayer);
    }

    /**
     * Forcibly saves the account.
     * @param uuid
     * The account to save
     * @return
     * true if saved, false if not saved, or null if not implemented
     */
    public static Boolean forceSave(UUID uuid, Boolean isPlayer) {
        return API.forceSave(uuid, isPlayer);
    }

    /**
     * Get the modid of the economy Grand Economy is using for currency.
     */
    public static String getEconomyModId() {
        return API.getEconomyModId();
    }

    private static final Map<String, IEconHandler> econHandlers = Maps.newHashMap();
    public static boolean hasEconHandler(String key) {
        return econHandlers.containsKey(key);
    }
    public static Map<String, IEconHandler> getEconHandlers() {
        return Collections.unmodifiableMap(econHandlers);
    }

    /**
     * Register an economy handler for your mod. This will allow setting the "economy bridge" config option to forModid or one of the aliases to have GE use your economy handler.
     * This must be done BEFORE {@link net.minecraftforge.fml.event.server.FMLServerStartedEvent} and {@link net.fabricmc.fabric.api.event.server.ServerStartCallback}.
     * @param handler
     * Your economy handler instance
     * @param forModid
     * The modid the handler is for.
     * @param aliases
     * Aliases that can also be used in the config to use your currency. The method still returns true if any of these are already taken, as long as the modid isn't.
     * @return false if registering the handler failed (currently the only reason is if another handler is registered with the given modid) and true otherwise
     */
    @SuppressWarnings({"JavadocReference", "UnusedReturnValue"})
    public static boolean registerEconomyHandler(IEconHandler handler, String forModid, String... aliases) {
        if(econHandlers.containsKey(forModid) || forModid.equalsIgnoreCase(MODID))
            return false;
        econHandlers.put(forModid, handler);
        for(String alias: aliases)
            econHandlers.putIfAbsent(alias, handler);
        return true;
    }
}
