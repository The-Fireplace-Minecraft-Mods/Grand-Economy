package the_fireplace.grandeconomy.econhandlers.ep;

import com.kamildanak.minecraft.enderpay.api.EnderPayApi;
import com.kamildanak.minecraft.enderpay.api.InsufficientCreditException;
import com.kamildanak.minecraft.enderpay.api.NoSuchAccountException;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class EnderPayEconHandler implements IEconHandler {
    @Override
    public long getBalance(UUID uuid) {
        try {
            return EnderPayApi.getBalance(uuid);
        } catch(NoSuchAccountException e) {
            return 0;
        }
    }

    @Override
    public void addToBalance(UUID uuid, long amount, boolean showMsg) {
        try {
            EnderPayApi.addToBalance(uuid, amount);
        } catch(NoSuchAccountException ignored) {}
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        try {
            EnderPayApi.takeFromBalance(uuid, amount);
            return true;
        } catch(NoSuchAccountException | InsufficientCreditException e) {
            return false;
        }
    }

    @Override
    public String getCurrencyName(long amount) {
        return EnderPayApi.getCurrencyName(amount);
    }

    @Override
    public void init() {

    }
}
