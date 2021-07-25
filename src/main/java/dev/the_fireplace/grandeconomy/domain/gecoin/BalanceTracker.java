package dev.the_fireplace.grandeconomy.domain.gecoin;

import java.util.UUID;

public interface BalanceTracker {
    double getBalance(UUID accountId);
    void setBalance(UUID accountId, double balance);
    void addToBalance(UUID accountId, double amount);
    boolean hasBalance(UUID playerId);
    void deleteBalance(UUID playerId);
}
