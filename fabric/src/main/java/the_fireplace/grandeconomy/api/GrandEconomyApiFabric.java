package the_fireplace.grandeconomy.api;

import the_fireplace.grandeconomy.fabric.GrandEconomy;
import the_fireplace.grandeconomy.fabric.api.event.BalanceChangeEvent;

import java.util.UUID;

public class GrandEconomyApiFabric implements IGrandEconomyApi {

    public GrandEconomyApiFabric() {
        GrandEconomyApi.setAPI(this);
    }

    @Override
    public long getBalance(UUID uuid, Boolean isPlayer) {
        return GrandEconomy.getEconomy().getBalance(uuid, isPlayer);
    }

    @Override
    public String getBalanceFormatted(UUID uuid, Boolean isPlayer) {
        return formatCurrency(GrandEconomy.getEconomy().getBalance(uuid, isPlayer));
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean added = GrandEconomy.getEconomy().addToBalance(uuid, amount, isPlayer);
        if(added)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = GrandEconomy.getEconomy().setBalance(uuid, amount, isPlayer);
        if(balanceSet)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean taken = GrandEconomy.getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if(taken)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return taken;
    }

    @Override
    public String getCurrencyName(long amount) {
        return GrandEconomy.getEconomy().getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(long amount) {
        return GrandEconomy.getEconomy().getFormattedCurrency(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return GrandEconomy.getEconomy().ensureAccountExists(uuid, isPlayer);
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        return GrandEconomy.getEconomy().forceSave(uuid, isPlayer);
    }

    @Override
    public String getEconomyModId() {
        return GrandEconomy.getEconomy().getId();
    }
}
