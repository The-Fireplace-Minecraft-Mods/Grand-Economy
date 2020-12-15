package the_fireplace.grandeconomy.nativeeconomy;

import com.google.gson.JsonObject;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.io.AccountData;
import the_fireplace.grandeconomy.io.JsonReader;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Account extends AccountData {
    private static final Map<UUID, Account> ACCOUNT_INSTANCES = new ConcurrentHashMap<>();

    public static Account get(UUID accountId) {
        return ACCOUNT_INSTANCES.computeIfAbsent(accountId, Account::new);
    }

    public static void delete(UUID accountId) {
        Account account = ACCOUNT_INSTANCES.remove(accountId);
        if(account != null)
            account.delete();
    }

    private double balance = GrandEconomy.config.startBalance;
    private boolean isPlayer = calculateIsPlayer();

    private Account(UUID uuid) {
        super(uuid, "account");
        loadSavedData();
    }

    double getBalance() {
        return balance;
    }

    void setBalance(double v) {
        if(balance != v) {
            balance = v;
            markChanged();
        }
    }

    void addBalance(double v) {
        setBalance(balance + v);
    }

    private boolean calculateIsPlayer() {
        return GrandEconomy.getServer().getPlayerManager().getPlayer(accountId) != null;
    }

    @Override
    public void readFromJson(JsonReader reader) {
        balance = reader.readDouble("balance", balance);
        isPlayer = reader.readBool("isPlayer", calculateIsPlayer());
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("balance", balance);
        obj.addProperty("isPlayer", isPlayer);
        return obj;
    }

    @Override
    protected boolean isDefaultData() {
        return balance == GrandEconomy.config.startBalance && !isPlayer;
    }
}
