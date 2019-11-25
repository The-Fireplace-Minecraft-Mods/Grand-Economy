package the_fireplace.grandeconomy.api.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

/**
 * Fired when an account's balance successfully changes.
 * This event is not {@link net.minecraftforge.eventbus.api.Cancelable}.
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 */
public class BalanceChangeEvent extends Event {
    private long oldBalance, newBalance;
    private UUID accountId;

    public BalanceChangeEvent(long oldBalance, long newBalance, UUID accountId) {
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.accountId = accountId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public long getOldBalance() {
        return oldBalance;
    }

    public long getNewBalance() {
        return newBalance;
    }
}
