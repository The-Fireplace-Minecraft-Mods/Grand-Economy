package dev.the_fireplace.grandeconomy.domain.tracker;

import java.util.UUID;

public interface LoginTracker {
    void addLogin(UUID playerId);
    long getDaysSinceLastLogin(UUID playerId);
    boolean hasLoggedInBefore(UUID playerId);
    void deleteLoginData(UUID playerId);
}
