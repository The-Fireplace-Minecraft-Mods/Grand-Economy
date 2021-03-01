package dev.the_fireplace.grandeconomy.nativeeconomy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {
	private static final Map<UUID, BalanceTracker> ACCOUNT_INSTANCES = new ConcurrentHashMap<>();

	public static BalanceTracker get(UUID accountId) {
	    return ACCOUNT_INSTANCES.computeIfAbsent(accountId, BalanceTracker::create);
	}

	public static boolean delete(UUID accountId) {
	    BalanceTracker account = ACCOUNT_INSTANCES.remove(accountId);
	    if (account != null) {
	        account.delete();
	        return true;
	    }

	    return false;
	}
}
