package com.kamildanak.minecraft.enderpay.api;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.economy.Account;

import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class EnderPayApi {
    public static long getBalance(UUID uuid) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        return account.getBalance();
    }

    public static void addToBalance(UUID uuid, long amount) throws NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        account.addBalance(amount);
    }

    public static void takeFromBalance(UUID uuid, long amount) throws InsufficientCreditException, NoSuchAccountException {
        Account account = Account.get(uuid);
        if (account == null) throw new NoSuchAccountException();
        if (account.getBalance() < amount) throw new InsufficientCreditException();
        account.addBalance(-amount);
    }

    public static void takeFromBalanceNegative(UUID uuid, long amount) throws NoSuchAccountException {
        addToBalance(uuid, -amount);
    }

    @Deprecated
    public static String getCurrencyNameSingular() {
        return EnderPay.settings.getCurrencyNameSingular();
    }

    @Deprecated
    public static String getCurrencyNameMultiple() {
        return EnderPay.settings.getCurrencyNameMultiple();
    }

    public static String getCurrencyName(long amount) {
        if (amount == 1) return EnderPay.settings.getCurrencyNameSingular();
        return EnderPay.settings.getCurrencyNameMultiple();
    }
}
