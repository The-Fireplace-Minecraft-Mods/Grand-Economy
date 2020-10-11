package the_fireplace.grandeconomy.requesthandler;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.event.BalanceChangeEvent;

import java.util.UUID;

public class GrandEconomyApiFabric implements IGrandEconomyApi {

    public GrandEconomyApiFabric() {
        GrandEconomyApi.setAPI(this);
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return GrandEconomy.getEconomy().getBalance(uuid, isPlayer);
    }

    @Override
    public String getBalanceFormatted(UUID uuid, Boolean isPlayer) {
        return formatCurrency(GrandEconomy.getEconomy().getBalance(uuid, isPlayer));
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean added = GrandEconomy.getEconomy().addToBalance(uuid, amount, isPlayer);
        if(added)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = GrandEconomy.getEconomy().setBalance(uuid, amount, isPlayer);
        if(balanceSet)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean taken = GrandEconomy.getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if(taken)
            BalanceChangeEvent.EVENT.invoker().onBalanceChanged(uuid, oldAmount, getBalance(uuid, isPlayer));
        return taken;
    }

    @Override
    public String getCurrencyName(double amount) {
        return GrandEconomy.getEconomy().getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(double amount) {
        return GrandEconomy.getEconomy().getFormattedCurrency(amount);
    }

    @Override
    public String getEconomyModId() {
        return GrandEconomy.getEconomy().getId();
    }
}
