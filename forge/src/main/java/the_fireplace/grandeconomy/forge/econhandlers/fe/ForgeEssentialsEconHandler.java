package the_fireplace.grandeconomy.forge.econhandlers.fe;

/*public class ForgeEssentialsEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).get();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).add(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)).withdraw(amount);
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        APIRegistry.economy.getWallet(UserIdent.get(uuid)).set(amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        return APIRegistry.economy.currency(amount);
    }

    @Override
    public String toString(double amount) {
        return APIRegistry.economy.toString(amount);
    }

    @Override
    public boolean ensureAccountExists(UUID uuid, Boolean isPlayer) {
        return APIRegistry.economy.getWallet(UserIdent.get(uuid)) != null;
    }

    @Override
    public Boolean forceSave(UUID uuid, Boolean isPlayer) {
        return null;
    }

    @Override
    public String getId() {
        return ForgeEssentials.MODID;
    }

    @Override
    public void init() {

    }
}*/
