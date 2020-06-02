package the_fireplace.grandeconomy.econhandlers.pixelmon;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

import java.util.UUID;

public class PixelmonEconHandler implements IEconHandler {
    private boolean hasAccount(UUID uuid) {
        return Pixelmon.moneyManager.getBankAccount(uuid).isPresent();
    }

    private IPixelmonBankAccount getAccount(UUID uuid) {
        IPixelmonBankAccount acct;
        if(Pixelmon.moneyManager.getBankAccount(uuid).isPresent())
            acct = Pixelmon.moneyManager.getBankAccount(uuid).get();
        else
            acct = new IPixelmonBankAccount() {
                @Override
                public int getMoney() {
                    return 0;
                }

                @Override
                public void setMoney(int i) {

                }

                @Override
                public int changeMoney(int i) {
                    return 0;
                }

                @Override
                public UUID getOwnerUUID() {
                    return null;
                }
            };
        return acct;
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return getAccount(uuid).getMoney();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        getAccount(uuid).changeMoney((int)amount);
        return hasAccount(uuid);
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        getAccount(uuid).changeMoney((int)-amount);
        return hasAccount(uuid);
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        getAccount(uuid).setMoney((int)amount);
        return hasAccount(uuid);
    }

    @Override
    public String getCurrencyName(double amount) {
        return "PokéDollar"+((int)amount != 1 ? 's' : "");
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return Pixelmon.MODID;
    }

    @Override
    public void init() {

    }
}
