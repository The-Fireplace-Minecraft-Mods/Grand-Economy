package dev.the_fireplace.grandeconomy.config;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageWriteBuffer;
import dev.the_fireplace.lib.api.storage.lazy.LazyConfig;
import dev.the_fireplace.lib.api.storage.lazy.LazyConfigInitializer;

public final class ModConfig extends LazyConfig {
    private static final ModConfig INSTANCE = LazyConfigInitializer.lazyInitialize(new ModConfig());
    private static final ModConfig DEFAULT_INSTANCE = new ModConfig();
    private final Access access = new Access();

    public static ModConfig getInstance() {
        return INSTANCE;
    }
    public static Access getData() {
        return INSTANCE.access;
    }
    static Access getDefaultData() {
        return DEFAULT_INSTANCE.access;
    }

    private ModConfig() {}
    
    private boolean showBalanceOnJoin = false;
    private double pvpMoneyTransferPercent = 0;
    private double pvpMoneyTransferFlat = 0;
    private boolean basicIncome = true;
    private double basicIncomeAmount = 50;
    private int maxIncomeSavingsDays = 5;

    private String economyHandler = GrandEconomy.MODID;
    private boolean enforceNonNegativeBalance = true;

    private String currencyNameSingular = "gp";
    private String currencyNameMultiple = "gp";
    private String decimalFormattingLanguageTag = "en-US";
    private double startBalance = 100;

    @Override
    public void readFrom(StorageReadBuffer reader) {
        showBalanceOnJoin = reader.readBool("showBalanceOnJoin", showBalanceOnJoin);
        pvpMoneyTransferPercent = reader.readDouble("pvpMoneyTransferPercent", pvpMoneyTransferPercent);
        pvpMoneyTransferFlat = reader.readDouble("pvpMoneyTransferFlat", pvpMoneyTransferFlat);
        basicIncome = reader.readBool("basicIncome", basicIncome);
        basicIncomeAmount = reader.readDouble("basicIncomeAmount", basicIncomeAmount);
        maxIncomeSavingsDays = reader.readInt("maxIncomeSavingsDays", maxIncomeSavingsDays);

        economyHandler = reader.readString("economyHandler", economyHandler);
        enforceNonNegativeBalance = reader.readBool("enforceNonNegativeBalance", enforceNonNegativeBalance);

        currencyNameSingular = reader.readString("currencyNameSingular", currencyNameSingular);
        currencyNameMultiple = reader.readString("currencyNameMultiple", currencyNameMultiple);
        decimalFormattingLanguageTag = reader.readString("decimalFormattingLanguageTag", decimalFormattingLanguageTag);
        startBalance = reader.readDouble("startBalance", startBalance);
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
        return GrandEconomy.MODID;
    }

    public void resave() {
        save();
    }
    
    public final class Access {

        public boolean isShowBalanceOnJoin() {
            return showBalanceOnJoin;
        }

        public void setShowBalanceOnJoin(boolean showBalanceOnJoin) {
            ModConfig.this.showBalanceOnJoin = showBalanceOnJoin;
        }

        public double getPvpMoneyTransferPercent() {
            return pvpMoneyTransferPercent;
        }

        public void setPvpMoneyTransferPercent(double pvpMoneyTransferPercent) {
            ModConfig.this.pvpMoneyTransferPercent = pvpMoneyTransferPercent;
        }

        public double getPvpMoneyTransferFlat() {
            return pvpMoneyTransferFlat;
        }

        public void setPvpMoneyTransferFlat(double pvpMoneyTransferFlat) {
            ModConfig.this.pvpMoneyTransferFlat = pvpMoneyTransferFlat;
        }

        public boolean isBasicIncome() {
            return basicIncome;
        }

        public void setBasicIncome(boolean basicIncome) {
            ModConfig.this.basicIncome = basicIncome;
        }

        public double getBasicIncomeAmount() {
            return basicIncomeAmount;
        }

        public void setBasicIncomeAmount(double basicIncomeAmount) {
            ModConfig.this.basicIncomeAmount = basicIncomeAmount;
        }

        public int getMaxIncomeSavingsDays() {
            return maxIncomeSavingsDays;
        }

        public void setMaxIncomeSavingsDays(int maxIncomeSavingsDays) {
            ModConfig.this.maxIncomeSavingsDays = maxIncomeSavingsDays;
        }

        public String getEconomyHandler() {
            return economyHandler;
        }

        public void setEconomyHandler(String economyHandler) {
            ModConfig.this.economyHandler = economyHandler;
        }

        public boolean isEnforceNonNegativeBalance() {
            return enforceNonNegativeBalance;
        }

        public void setEnforceNonNegativeBalance(boolean enforceNonNegativeBalance) {
            ModConfig.this.enforceNonNegativeBalance = enforceNonNegativeBalance;
        }

        public String getCurrencyNameSingular() {
            return currencyNameSingular;
        }

        public void setCurrencyNameSingular(String currencyNameSingular) {
            ModConfig.this.currencyNameSingular = currencyNameSingular;
        }

        public String getCurrencyNameMultiple() {
            return currencyNameMultiple;
        }

        public void setCurrencyNameMultiple(String currencyNameMultiple) {
            ModConfig.this.currencyNameMultiple = currencyNameMultiple;
        }

        public String getDecimalFormattingLanguageTag() {
            return decimalFormattingLanguageTag;
        }

        public void setDecimalFormattingLanguageTag(String decimalFormattingLanguageTag) {
            ModConfig.this.decimalFormattingLanguageTag = decimalFormattingLanguageTag;
        }

        public double getStartBalance() {
            return startBalance;
        }

        public void setStartBalance(double startBalance) {
            ModConfig.this.startBalance = startBalance;
        }
    }
}
