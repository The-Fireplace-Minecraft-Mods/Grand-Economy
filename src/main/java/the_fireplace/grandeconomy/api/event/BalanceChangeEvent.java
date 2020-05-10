package the_fireplace.grandeconomy.api.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

/**
 * Fired when an account's balance successfully changes.
 * This event is not {@link Cancelable}.
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 */
public class BalanceChangeEvent extends Event {
    private final long oldBalance;
    private final long newBalance;
    private final UUID accountId;

    public BalanceChangeEvent(final long oldBalance, final long newBalance, final UUID accountId) {
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.accountId = accountId;
    }

    public final UUID getAccountId() {
        return accountId;
    }

    public final long getOldBalance() {
        return oldBalance;
    }

    public final long getNewBalance() {
        return newBalance;
    }
}
