package the_fireplace.grandeconomy.forge.api.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

/**
 * Fired when an account's balance successfully changes.
 * This event is not {@link net.minecraftforge.eventbus.api.Cancelable}.
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 */
public class BalanceChangeEvent extends Event {
    private final double oldBalance, newBalance;
    private final UUID accountId;

    public BalanceChangeEvent(double oldBalance, double newBalance, UUID accountId) {
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.accountId = accountId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }
}
