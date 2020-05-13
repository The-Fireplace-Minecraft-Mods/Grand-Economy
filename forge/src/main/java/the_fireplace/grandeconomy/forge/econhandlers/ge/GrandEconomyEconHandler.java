package the_fireplace.grandeconomy.forge.econhandlers.ge;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.forge.Config;
import the_fireplace.grandeconomy.forge.GrandEconomy;

import java.util.UUID;

public class GrandEconomyEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return 0;
        }
        account.update();
        return account.getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if(amount < 0)
            return false;

        account.setBalance(amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        if (amount == 1)
            return Config.currencyNameSingular;
        return Config.currencyNameMultiple;
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomyApi.MODID;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
