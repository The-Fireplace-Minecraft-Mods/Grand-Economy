package the_fireplace.grandeconomy.api;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.IGrandEconomyApi;
import the_fireplace.grandeconomy.forge.GrandEconomy;
import the_fireplace.grandeconomy.forge.api.event.BalanceChangeEvent;
import the_fireplace.grandeconomy.api.IEconHandler;

import java.util.UUID;

@SuppressWarnings("unused")
public class GrandEconomyApiForge implements IGrandEconomyApi {

    public GrandEconomyApiForge() {
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
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return added;
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean balanceSet = GrandEconomy.getEconomy().setBalance(uuid, amount, isPlayer);
        if(balanceSet)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return balanceSet;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, Boolean isPlayer) {
        long oldAmount = getBalance(uuid, isPlayer);
        boolean taken = GrandEconomy.getEconomy().takeFromBalance(uuid, amount, isPlayer);
        if(taken)
            MinecraftForge.EVENT_BUS.post(new BalanceChangeEvent(oldAmount, getBalance(uuid, isPlayer), uuid));
        return taken;
    }

    @Override
    public String getCurrencyName(long amount) {
        return GrandEconomy.getEconomy().getCurrencyName(amount);
    }

    @Override
    public String formatCurrency(long amount) {
        return GrandEconomy.getEconomy().toString(amount);
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

    @Override
    public boolean registerEconomyHandler(IEconHandler handler, String forModid, String... aliases) {
        return GrandEconomy.registerEconHandler(handler, forModid, aliases);
    }
}
