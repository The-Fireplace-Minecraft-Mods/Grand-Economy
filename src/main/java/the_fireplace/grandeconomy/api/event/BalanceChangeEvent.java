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
    private final double oldBalance;
    private final double newBalance;
    private final UUID accountId;

    public BalanceChangeEvent(final double oldBalance, final double newBalance, final UUID accountId) {
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.accountId = accountId;
    }

    public final UUID getAccountId() {
        return accountId;
    }

    public final double getOldBalance() {
        return oldBalance;
    }

    public final double getNewBalance() {
        return newBalance;
    }
}
