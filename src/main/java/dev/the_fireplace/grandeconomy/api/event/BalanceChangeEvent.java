package dev.the_fireplace.grandeconomy.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.UUID;

public interface BalanceChangeEvent {
    Event<BalanceChangeEvent> EVENT = EventFactory.createArrayBacked(BalanceChangeEvent.class,
        (listeners) -> (accountId, oldBalance, newBalance) -> {
            for(BalanceChangeEvent event: listeners)
                event.onBalanceChanged(accountId, oldBalance, newBalance);
        });

    void onBalanceChanged(UUID accountId, double oldBalance, double newBalance);
}
