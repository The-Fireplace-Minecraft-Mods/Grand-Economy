package the_fireplace.grandeconomy.proxy;

public class DummySettings implements ISettings{
    private String currencyNameSingular;
    private String currencyNameMultiple;
    private long maxLoginDelta;
    private boolean basicIncome;
    private int basicIncomeAmount;
    private boolean stampedMoney;
    private int stampedMoneyPercent;
    private int startBalance;
    private int resetLoginDelta;
    private int dayLength;
    private int pvpMoneyDrop;

    public DummySettings(String currencyNameSingular,
                         String currencyNameMultiple,
                         long maxLoginDelta,
                         boolean basicIncome,
                         int basicIncomeAmount,
                         boolean stampedMoney,
                         int stampedMoneyPercent,
                         int startBalance,
                         int resetLoginDelta,
                         int dayLength,
                         int pvpMoneyDrop) {
        this.currencyNameSingular = currencyNameSingular;
        this.currencyNameMultiple = currencyNameMultiple;
        this.maxLoginDelta = maxLoginDelta;
        this.basicIncome = basicIncome;
        this.basicIncomeAmount = basicIncomeAmount;
        this.stampedMoney = stampedMoney;
        this.stampedMoneyPercent = stampedMoneyPercent;
        this.startBalance = startBalance;
        this.resetLoginDelta = resetLoginDelta;
        this.dayLength = dayLength;
        this.pvpMoneyDrop = pvpMoneyDrop;
    }

    @Override
    public String getCurrencyNameSingular() {
        return currencyNameSingular;
    }

    @Override
    public String getCurrencyNameMultiple() {
        return currencyNameMultiple;
    }

    @Override
    public long getMaxLoginDelta() {
        return maxLoginDelta;
    }

    @Override
    public boolean isBasicIncome() {
        return basicIncome;
    }

    @Override
    public int getBasicIncomeAmount() {
        return basicIncomeAmount;
    }

    @Override
    public boolean isStampedMoney() {
        return stampedMoney;
    }

    @Override
    public int getStampedMoneyPercent() {
        return stampedMoneyPercent;
    }

    @Override
    public int getStartBalance() {
        return startBalance;
    }

    @Override
    public int getResetLoginDelta() {
        return resetLoginDelta;
    }

    @Override
    public int getDayLength() {
        return dayLength;
    }

    @Override
    public int getPvpMoneyDrop() {
        return pvpMoneyDrop;
    }
}
