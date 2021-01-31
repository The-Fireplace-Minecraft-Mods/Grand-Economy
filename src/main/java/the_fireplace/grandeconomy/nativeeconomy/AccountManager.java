package the_fireplace.grandeconomy.nativeeconomy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountManager {
	private static final Map<UUID, Account> ACCOUNT_INSTANCES = new ConcurrentHashMap<>();

	public static Account get(UUID accountId) {
	    return ACCOUNT_INSTANCES.computeIfAbsent(accountId, Account::new);
	}

	public static boolean delete(UUID accountId) {
	    Account account = ACCOUNT_INSTANCES.remove(accountId);
	    if (account != null) {
	        account.delete();
	        return true;
	    }

	    return false;
	}
}
