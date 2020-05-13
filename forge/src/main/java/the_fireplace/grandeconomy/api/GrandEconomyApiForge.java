package the_fireplace.grandeconomy.api;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.forge.GrandEconomy;
import the_fireplace.grandeconomy.forge.api.event.BalanceChangeEvent;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApiForge implements IGrandEconomyApi {

    public GrandEconomyApiForge() {
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
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = GrandEconomy.getEconomy().setBalance(uuid, amount, isPlayer);
        if(balanceSet)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        double oldAmount = getBalance(uuid, isPlayer);
        boolean taken = GrandEconomy.getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if(taken)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
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
