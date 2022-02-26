package dev.the_fireplace.grandeconomy.eventtriggers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.events.BalanceChangedEvent;
import dev.the_fireplace.grandeconomy.domain.eventtrigger.BalanceChangedTrigger;
import dev.the_fireplace.lib.api.events.FLEventBus;

import java.util.UUID;

@Implementation
public final class ForgeBalanceChangedTrigger implements BalanceChangedTrigger
{
    @Override
    public void triggerBalanceChanged(UUID accountId, double oldBalance, double newBalance) {
        FLEventBus.BUS.post(new BalanceChangedEvent(accountId, oldBalance, newBalance));
    }
}
