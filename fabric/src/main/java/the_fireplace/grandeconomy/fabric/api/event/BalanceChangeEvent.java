package the_fireplace.grandeconomy.fabric.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.UUID;

public interface BalanceChangeEvent {
    Event<BalanceChangeEvent> EVENT = EventFactory.createArrayBacked(BalanceChangeEvent.class,
        (listeners) -> (accountId, oldBalance, newBalance) -> {
            for(BalanceChangeEvent event: listeners)
                event.onBalanceChanged(accountId, oldBalance, newBalance);
        });

    void onBalanceChanged(UUID accountId, long oldBalance, long newBalance);
}
