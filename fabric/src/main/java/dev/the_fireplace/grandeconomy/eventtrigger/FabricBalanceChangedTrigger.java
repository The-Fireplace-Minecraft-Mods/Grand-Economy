package dev.the_fireplace.grandeconomy.eventtrigger;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.BalanceChangeEvent;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.BalanceChangedTrigger;

import java.util.UUID;

@Implementation
public final class FabricBalanceChangedTrigger implements BalanceChangedTrigger
{
    @Override
    public void triggerBalanceChanged(UUID accountId, double oldBalance, double newBalance) {
        BalanceChangeEvent.EVENT.invoker().onBalanceChanged(accountId, oldBalance, newBalance);
    }
}
