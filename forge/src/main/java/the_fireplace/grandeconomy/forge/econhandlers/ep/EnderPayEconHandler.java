package the_fireplace.grandeconomy.forge.econhandlers.ep;

/*public class EnderPayEconHandler implements IEconHandler {
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
            EnderPayApi.addToBalance(uuid, amount);
            return true;
        } catch(NoSuchAccountException ignored) {
            return false;
        }
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        try {
            EnderPayApi.takeFromBalance(uuid, amount);
            return true;
        } catch(NoSuchAccountException | InsufficientCreditException e) {
            return false;
        }
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        try {
            EnderPayApi.takeFromBalance(uuid, EnderPayApi.getBalance(uuid));
            EnderPayApi.addToBalance(uuid, amount);
            return true;
        } catch(NoSuchAccountException | InsufficientCreditException e) {
            return false;
        }
    }

    @Override
    public String getCurrencyName(double amount) {
        return EnderPayApi.getCurrencyName(amount);
    }

    @Override
    public String toString(double amount) {
        return amount + ' ' + getCurrencyName(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return Account.get(uuid) != null;
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        try {
            Objects.requireNonNull(Account.get(uuid)).writeIfChanged();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    @Override
    public String getId() {
        return EnderPay.modID;
    }

    @Override
    public void init() {

    }
}*/
