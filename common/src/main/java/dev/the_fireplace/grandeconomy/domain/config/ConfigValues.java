package dev.the_fireplace.grandeconomy.domain.config;

public interface ConfigValues {
    boolean isShowBalanceOnJoin();

    double getPvpMoneyTransferPercent();

    double getPvpMoneyTransferFlat();

    boolean isBasicIncome();

    double getBasicIncomeAmount();

    int getMaxIncomeSavingsDays();

    String getEconomyHandler();

    boolean isEnforceNonNegativeBalance();

    String getCurrencyNameSingular();

    String getCurrencyNameMultiple();

    String getDecimalFormattingLanguageTag();

    double getStartBalance();
}
