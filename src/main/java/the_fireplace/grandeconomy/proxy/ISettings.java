package the_fireplace.grandeconomy.proxy;

public interface ISettings {
    String getCurrencyNameSingular();

    String getCurrencyNameMultiple();

    long getMaxLoginDelta();

    boolean isBasicIncome();

    int getBasicIncomeAmount();

    boolean isStampedMoney();

    int getStampedMoneyPercent();

    int getStartBalance();

    int getResetLoginDelta();

    int getDayLength();

    int getPvpMoneyDrop();
}
