package the_fireplace.grandeconomy.econhandlers.ep;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.api.EnderPayApi;
import com.kamildanak.minecraft.enderpay.api.InsufficientCreditException;
import com.kamildanak.minecraft.enderpay.api.NoSuchAccountException;
import com.kamildanak.minecraft.enderpay.economy.Account;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class EnderPayEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        try {
            return EnderPayApi.getBalance(uuid);
        } catch(NoSuchAccountException e) {
            return 0;
        }
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        try {
            EnderPayApi.addToBalance(uuid, (long) amount);
            if(!Boolean.TRUE.equals(isPlayer))
                forceSave(uuid);
            return true;
        } catch(NoSuchAccountException ignored) {
            return false;
        }
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        try {
            EnderPayApi.takeFromBalance(uuid, (long) amount);
            if(!Boolean.TRUE.equals(isPlayer))
                forceSave(uuid);
            return true;
        } catch(NoSuchAccountException | InsufficientCreditException e) {
            return false;
        }
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        try {
            EnderPayApi.takeFromBalance(uuid, EnderPayApi.getBalance(uuid));
            EnderPayApi.addToBalance(uuid, (long) amount);
            if(!Boolean.TRUE.equals(isPlayer))
                forceSave(uuid);
            return true;
        } catch(NoSuchAccountException | InsufficientCreditException e) {
            return false;
        }
    }

    @Override
    public String getCurrencyName(double amount) {
        return EnderPayApi.getCurrencyName((long) amount);
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return new DecimalFormat("#"+ GrandEconomy.nativeConfig.thousandsSeparator+"###").format(amount) + " " + getCurrencyName(amount);
    }

    private void forceSave(UUID uuid) {
        try {
            Objects.requireNonNull(Account.get(uuid)).writeIfChanged();
        } catch(IOException ignored) {}
    }

    @Override
    public String getId() {
        return EnderPay.modID;
    }

    @Override
    public void init() {

    }
}
