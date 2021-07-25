package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;

import javax.inject.Singleton;

@Implementation(name="default")
@Singleton
public final class GEConfigDefaults implements ConfigValues {
    @Override
    public boolean isShowBalanceOnJoin() {
        return false;
    }

    @Override
    public double getPvpMoneyTransferPercent() {
        return 0;
    }

    @Override
    public double getPvpMoneyTransferFlat() {
        return 0;
    }

    @Override
    public boolean isBasicIncome() {
        return true;
    }

    @Override
    public double getBasicIncomeAmount() {
        return 50;
    }

    @Override
    public int getMaxIncomeSavingsDays() {
        return 5;
    }

    @Override
    public String getEconomyHandler() {
        return GrandEconomyConstants.MODID;
    }

    @Override
    public boolean isEnforceNonNegativeBalance() {
        return true;
    }

    @Override
    public String getCurrencyNameSingular() {
        return "GECoin";
    }

    @Override
    public String getCurrencyNameMultiple() {
        return "GECoins";
    }

    @Override
    public String getDecimalFormattingLanguageTag() {
        return "en-US";
    }

    @Override
    public double getStartBalance() {
        return 100;
    }
}
