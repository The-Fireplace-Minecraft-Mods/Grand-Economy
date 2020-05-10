package the_fireplace.grandeconomy.econhandlers.ge;

import net.minecraftforge.common.MinecraftForge;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class GrandEconomyEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return 0;
        }
        account.update();
        return account.getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        if(!Boolean.TRUE.equals(isPlayer))
            forceSave(uuid);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        if(!Boolean.TRUE.equals(isPlayer))
            forceSave(uuid);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for %s was null", uuid.toString());
            return false;
        }
        if(amount < 0)
            return false;

        account.setBalance(amount);
        if(!Boolean.TRUE.equals(isPlayer))
            forceSave(uuid);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        if (amount == 1)
            return GrandEconomy.nativeConfig.currencyNameSingular;
        return GrandEconomy.nativeConfig.currencyNameMultiple;
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return new DecimalFormat("#"+ GrandEconomy.nativeConfig.thousandsSeparator+"###").format(amount) + " " + getCurrencyName(amount);
    }

    private void forceSave(UUID uuid) {
        try {
            Objects.requireNonNull(Account.get(uuid)).writeIfChanged();
        } catch(IOException ignored) {}
    }

    @Override
    public String getId() {
        return GrandEconomy.MODID;
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
