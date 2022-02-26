package dev.the_fireplace.grandeconomy.adapters;

import com.gmail.sneakdevs.diamondeconomy.DatabaseManager;
import com.mojang.authlib.GameProfile;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.lib.api.player.injectables.GameProfileFinder;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public final class DiamondEconomyAdapter implements EconomyAdapter
{
    private final GameProfileFinder gameProfileFinder;
    private DatabaseManager databaseManager;

    @Inject
    public DiamondEconomyAdapter(GameProfileFinder gameProfileFinder) {
        this.gameProfileFinder = gameProfileFinder;
    }

    private String findName(UUID uuid, Boolean isPlayer) {
        String nonPlayerName = "Grand Economy Non-Player " + uuid.toString();
        if (Boolean.FALSE.equals(isPlayer)) {
            return nonPlayerName;
        }

        Optional<GameProfile> playerProfile = gameProfileFinder.findProfile(uuid);
        if (playerProfile.isEmpty()) {
            return nonPlayerName;
        }

        return playerProfile.get().getName();
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return databaseManager.getBalanceFromUUID(uuid.toString());
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        int previousBalance = databaseManager.getBalanceFromUUID(uuid.toString());
        int newBalance = previousBalance + (int) amount;
        databaseManager.setBalance(uuid.toString(), findName(uuid, isPlayer), newBalance);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        int previousBalance = databaseManager.getBalanceFromUUID(uuid.toString());
        int newBalance = Math.max(previousBalance - (int) amount, 0);
        databaseManager.setBalance(uuid.toString(), findName(uuid, isPlayer), newBalance);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        databaseManager.setBalance(uuid.toString(), findName(uuid, isPlayer), (int) amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return (int) amount != 1 ? "diamonds" : "diamond";
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return (int) amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return com.gmail.sneakdevs.diamondeconomy.DiamondEconomy.MODID;
    }

    @Override
    public void init() {
        this.databaseManager = new DatabaseManager();
    }
}
