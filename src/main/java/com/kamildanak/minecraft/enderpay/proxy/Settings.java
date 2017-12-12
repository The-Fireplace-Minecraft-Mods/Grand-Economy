package com.kamildanak.minecraft.enderpay.proxy;

import net.minecraftforge.common.config.Configuration;

public class Settings implements ISettings{
    public Configuration config;
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

    @SuppressWarnings("WeakerAccess")
    public Settings() {

    }

    public void loadConfig(Configuration config) {
        this.config = config;
        currencyNameSingular = config.getString("currency name (singular)", "general", "credit",
                "Currency name (max 20 char)");
        currencyNameMultiple = config.getString("currency name (multiple)", "general", "credits",
                "Currency name (max 20 char)");
        maxLoginDelta = (1000 * 60 * 60) * config.getInt("maxLoginDelta", "basicIncome", 6, 1, 20,
                "Maximum number of day since last login the player will be payed for. ");
        basicIncome = config.getBoolean("enabled", "basicIncome", true,
                "Each day give set amount of credits to each player to stimulate economy");
        basicIncomeAmount = config.getInt("amount", "basicIncome", 50, 0, 10000,
                "Amount of credits to give each player each day");
        stampedMoney = config.getBoolean("enabled", "stampedMoney", true,
                "Take % of players money each day to stimulate economy");
        stampedMoneyPercent = config.getInt("percent", "stampedMoney", 1, 0, 100,
                "What percentage of players money should be taken each day");
        startBalance = config.getInt("startBalance", "general", 100, 0, 10000,
                "Amount of credits given to new players joining the server");

        resetLoginDelta = config.getInt("resetLoginDelta", "basicIncome", 100, 1, 100,
                "Number of days of inactivity after account balance will be set to startBalance");

        dayLength = config.getInt("dayLength", "basicIncome", 24 * 60, 1, 24 * 60 * 365,
                "Day length in minutes");

        pvpMoneyDrop = config.getInt("pvpMoneyDrop", "general", 0, -2147483647,
                100, "What percentage (0-100) or what amount (pvpMoneyDrop<0)" +
                        " of players money should be transferred to slayer");
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

    public void reloadConfig() {
        loadConfig(config);
    }
}
