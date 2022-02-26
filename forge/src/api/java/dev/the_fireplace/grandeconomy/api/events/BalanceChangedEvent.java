package dev.the_fireplace.grandeconomy.api.events;

import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

/**
 * Event which is called every time an account's balance is changed.
 * Subscribe to this event on {@link dev.the_fireplace.lib.api.events.FLEventBus#BUS}.
 */
public final class BalanceChangedEvent extends Event
{
    private final UUID accountId;
    private final double oldBalance;
    private final double newBalance;

    public BalanceChangedEvent(UUID accountId, double oldBalance, double newBalance) {
        this.accountId = accountId;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
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
