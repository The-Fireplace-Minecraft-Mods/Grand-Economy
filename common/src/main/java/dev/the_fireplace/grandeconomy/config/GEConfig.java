package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.domain.config.ConfigValues;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Implementation("dev.the_fireplace.grandeconomy.domain.config.ConfigValues")
@Singleton
public final class GEConfig implements Config, ConfigValues {
    private final ConfigValues defaultConfig;

    private boolean showBalanceOnJoin;
    private double pvpMoneyTransferPercent;
    private double pvpMoneyTransferFlat;
    private boolean basicIncome;
    private double basicIncomeAmount;
    private int maxIncomeSavingsDays;

    private String economyHandler;
    private boolean enforceNonNegativeBalance;

    private String currencyNameSingular;
    private String currencyNameMultiple;
    private String decimalFormattingLanguageTag;
    private double startBalance;

    @Inject
    public GEConfig(ConfigStateManager configStateManager, @Named("default") ConfigValues defaultConfig) {
        this.defaultConfig = defaultConfig;
        configStateManager.initialize(this);
    }

    @Override
    public void readFrom(StorageReadBuffer reader) {
        showBalanceOnJoin = reader.readBool("showBalanceOnJoin", defaultConfig.isShowBalanceOnJoin());
        pvpMoneyTransferPercent = reader.readDouble("pvpMoneyTransferPercent", defaultConfig.getPvpMoneyTransferPercent());
        pvpMoneyTransferFlat = reader.readDouble("pvpMoneyTransferFlat", defaultConfig.getPvpMoneyTransferFlat());
        basicIncome = reader.readBool("basicIncome", defaultConfig.isBasicIncome());
        basicIncomeAmount = reader.readDouble("basicIncomeAmount", defaultConfig.getBasicIncomeAmount());
        maxIncomeSavingsDays = reader.readInt("maxIncomeSavingsDays", defaultConfig.getMaxIncomeSavingsDays());

        economyHandler = reader.readString("economyHandler", defaultConfig.getEconomyHandler());
        enforceNonNegativeBalance = reader.readBool("enforceNonNegativeBalance", defaultConfig.isEnforceNonNegativeBalance());

        currencyNameSingular = reader.readString("currencyNameSingular", defaultConfig.getCurrencyNameSingular());
        currencyNameMultiple = reader.readString("currencyNameMultiple", defaultConfig.getCurrencyNameMultiple());
        decimalFormattingLanguageTag = reader.readString("decimalFormattingLanguageTag", defaultConfig.getDecimalFormattingLanguageTag());
        startBalance = reader.readDouble("startBalance", defaultConfig.getStartBalance());
    }

    @Override
    public void writeTo(StorageWriteBuffer writer) {
        writer.writeBool("showBalanceOnJoin", showBalanceOnJoin);
        writer.writeDouble("pvpMoneyTransferPercent", pvpMoneyTransferPercent);
        writer.writeDouble("pvpMoneyTransferFlat", pvpMoneyTransferFlat);
        writer.writeBool("basicIncome", basicIncome);
        writer.writeDouble("basicIncomeAmount", basicIncomeAmount);
        writer.writeInt("maxIncomeSavingsDays", maxIncomeSavingsDays);

        writer.writeString("economyHandler", economyHandler);
        writer.writeBool("enforceNonNegativeBalance", enforceNonNegativeBalance);

        writer.writeString("currencyNameSingular", currencyNameSingular);
        writer.writeString("currencyNameMultiple", currencyNameMultiple);
        writer.writeString("decimalFormattingLanguageTag", decimalFormattingLanguageTag);
        writer.writeDouble("startBalance", startBalance);
    }

    @Override
    public String getId() {
        return GrandEconomyConstants.MODID;
    }

    @Override
    public boolean isShowBalanceOnJoin() {
        return showBalanceOnJoin;
    }

    public void setShowBalanceOnJoin(boolean showBalanceOnJoin) {
        GEConfig.this.showBalanceOnJoin = showBalanceOnJoin;
    }

    @Override
    public double getPvpMoneyTransferPercent() {
        return pvpMoneyTransferPercent;
    }

    public void setPvpMoneyTransferPercent(double pvpMoneyTransferPercent) {
        GEConfig.this.pvpMoneyTransferPercent = pvpMoneyTransferPercent;
    }

    @Override
    public double getPvpMoneyTransferFlat() {
        return pvpMoneyTransferFlat;
    }

    public void setPvpMoneyTransferFlat(double pvpMoneyTransferFlat) {
        GEConfig.this.pvpMoneyTransferFlat = pvpMoneyTransferFlat;
    }

    @Override
    public boolean isBasicIncome() {
        return basicIncome;
    }

    public void setBasicIncome(boolean basicIncome) {
        GEConfig.this.basicIncome = basicIncome;
    }

    @Override
    public double getBasicIncomeAmount() {
        return basicIncomeAmount;
    }

    public void setBasicIncomeAmount(double basicIncomeAmount) {
        GEConfig.this.basicIncomeAmount = basicIncomeAmount;
    }

    @Override
    public int getMaxIncomeSavingsDays() {
        return maxIncomeSavingsDays;
    }

    public void setMaxIncomeSavingsDays(int maxIncomeSavingsDays) {
        GEConfig.this.maxIncomeSavingsDays = maxIncomeSavingsDays;
    }

    @Override
    public String getEconomyHandler() {
        return economyHandler;
    }

    public void setEconomyHandler(String economyHandler) {
        GEConfig.this.economyHandler = economyHandler;
    }

    @Override
    public boolean isEnforceNonNegativeBalance() {
        return enforceNonNegativeBalance;
    }

    public void setEnforceNonNegativeBalance(boolean enforceNonNegativeBalance) {
        GEConfig.this.enforceNonNegativeBalance = enforceNonNegativeBalance;
    }

    @Override
    public String getCurrencyNameSingular() {
        return currencyNameSingular;
    }

    public void setCurrencyNameSingular(String currencyNameSingular) {
        GEConfig.this.currencyNameSingular = currencyNameSingular;
    }

    @Override
    public String getCurrencyNameMultiple() {
        return currencyNameMultiple;
    }

    public void setCurrencyNameMultiple(String currencyNameMultiple) {
        GEConfig.this.currencyNameMultiple = currencyNameMultiple;
    }

    @Override
    public String getDecimalFormattingLanguageTag() {
        return decimalFormattingLanguageTag;
    }

    public void setDecimalFormattingLanguageTag(String decimalFormattingLanguageTag) {
        GEConfig.this.decimalFormattingLanguageTag = decimalFormattingLanguageTag;
    }

    @Override
    public double getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(double startBalance) {
        GEConfig.this.startBalance = startBalance;
    }
}
