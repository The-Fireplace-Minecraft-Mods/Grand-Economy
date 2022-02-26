package dev.the_fireplace.grandeconomy.domain.eventtrigger;

import java.util.UUID;

public interface BalanceChangedTrigger
{
    void triggerBalanceChanged(UUID accountId, double oldBalance, double newBalance);
}
