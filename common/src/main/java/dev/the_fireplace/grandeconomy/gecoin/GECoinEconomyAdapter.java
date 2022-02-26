package dev.the_fireplace.grandeconomy.gecoin;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.grandeconomy.domain.gecoin.BalanceTracker;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

@Singleton
public final class GECoinEconomyAdapter implements EconomyAdapter
{
    private static final char NARROW_NO_BREAK_SPACE = '\u202F';

    private final BalanceTracker balanceTracker;
    private final ConfigValues configValues;

    @Inject
    public GECoinEconomyAdapter(BalanceTracker balanceTracker, ConfigValues configValues) {
        this.balanceTracker = balanceTracker;
        this.configValues = configValues;
    }
    
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return balanceTracker.getBalance(uuid);
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (balanceTracker.getBalance(uuid) + amount < 0) {
            return false;
        }

        balanceTracker.addToBalance(uuid, amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (balanceTracker.getBalance(uuid) < amount) {
            return false;
        }

        balanceTracker.addToBalance(uuid, -amount);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        if (amount < 0) {
            return false;
        }

        balanceTracker.setBalance(uuid, amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        if (amount == 1) {
            return configValues.getCurrencyNameSingular();
        }

        return configValues.getCurrencyNameMultiple();
    }

    @Override
    public String getFormattedCurrency(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.forLanguageTag(configValues.getDecimalFormattingLanguageTag())));
        return decimalFormat.format(amount).replace(NARROW_NO_BREAK_SPACE, ' ') + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomyConstants.MODID;
    }

    @Override
    public void init() {

    }
}
