package dev.the_fireplace.grandeconomy.nativeeconomy;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.Economy;
import dev.the_fireplace.grandeconomy.config.ModConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

public class NativeEconomy implements Economy {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return AccountManager.get(uuid).getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        BalanceTracker account = AccountManager.get(uuid);
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        BalanceTracker account = AccountManager.get(uuid);
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
            return ModConfig.getData().getCurrencyNameSingular();
        return ModConfig.getData().getCurrencyNameMultiple();
    }

    @Override
    public String getFormattedCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.forLanguageTag(ModConfig.getData().getDecimalFormattingLanguageTag())));
        return decimalFormat.format(amount).replace('\u202F', ' ') + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomy.MODID;
    }

    @Override
    public void init() {

    }
}
