package the_fireplace.grandeconomy.nativeeconomy;

import com.google.gson.JsonObject;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.io.AccountData;
import the_fireplace.lib.api.io.JsonObjectReader;
import the_fireplace.lib.impl.FireplaceLib;

import java.util.UUID;

public class Account extends AccountData {
    private double balance = GrandEconomy.getConfig().startBalance;
    private boolean isPlayer = calculateIsPlayer();

    Account(UUID uuid) {
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
        return FireplaceLib.getServer().getPlayerManager().getPlayer(getId()) != null;
    }

    @Override
    public void readFromJson(JsonObjectReader reader) {
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
        return balance == GrandEconomy.getConfig().startBalance && !isPlayer;
    }

    void delete() {
        deleteSaveFile();
    }
}
