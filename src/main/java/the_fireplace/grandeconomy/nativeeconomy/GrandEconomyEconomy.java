package the_fireplace.grandeconomy.nativeeconomy;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.Economy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

public class GrandEconomyEconomy implements Economy {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return AccountManager.get(uuid).getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = AccountManager.get(uuid);
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = AccountManager.get(uuid);
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (amount < 0)
            return false;

        AccountManager.get(uuid).setBalance(amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        if (amount == 1)
            return GrandEconomy.config.currencyNameSingular;
        return GrandEconomy.config.currencyNameMultiple;
    }

    @Override
    public String getFormattedCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.forLanguageTag(GrandEconomy.config.decimalFormattingLanguageTag)));
        return decimalFormat.format(amount) + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomy.MODID;
    }

    @Override
    public void init() {

    }
}
