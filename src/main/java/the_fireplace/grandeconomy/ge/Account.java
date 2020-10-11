package the_fireplace.grandeconomy.ge;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.utils.TimeUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Account {
    private static final HashMap<String, Account> accounts = new HashMap<>();
    private static File location;
    private boolean changed;

    private UUID uuid;
    private double balance;
    private long lastLogin;
    private long lastCountActivity;
    private boolean isPlayer = false;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = ModConfig.startBalance;
        long now = TimeUtils.getCurrentDay();
        this.lastLogin = now;
        this.lastCountActivity = now;
        this.changed = true;
        if(GrandEconomy.getServer().getUserCache().getByUuid(uuid) != null)
            this.isPlayer = true;
    }

    public static Account get(PlayerEntity player) {
        return get(player.getUuid());
    }

    public static Account get(UUID uuid) {
        Account account = accounts.get(uuid.toString());
        if (account != null)
            return account;

        if (location == null)
            return null;
        //noinspection ResultOfMethodCallIgnored
        location.mkdirs();

        account = new Account(uuid);
        accounts.put(uuid.toString(), account);

        File file = account.getFile();
        if (!file.exists()) return account;

        account.read();
        return account;
    }

    public static void clear() {
        Account.location = null;
        Account.accounts.clear();
    }

    public static void setLocation(File location) {
        Account.location = location;
    }

    boolean update() {
        long now = TimeUtils.getCurrentDay();
        long activityDeltaDays = now - this.lastCountActivity;
        this.lastCountActivity = now;

        if (!isPlayer || activityDeltaDays == 0) return false;

        if (ModConfig.basicIncome && getPlayerMP() != null) {
            long loginDeltaDays = (now - this.lastLogin);
            if (loginDeltaDays > ModConfig.maxIncomeSavingsDays)
                loginDeltaDays = ModConfig.maxIncomeSavingsDays;
            this.lastLogin = now;
            this.balance += loginDeltaDays * ModConfig.basicIncomeAmount;
        }
        return activityDeltaDays > 0;
    }

    void writeIfChanged() throws IOException {
        if (changed) write();
    }

    private File getFile() {
        return new File(location, uuid + ".json");
    }

    private void read() {
        read(getFile());
    }

    private void read(File file) {
        changed = false;

        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(file));
            JsonObject jsonObject = (JsonObject) obj;
            balance = jsonObject.get("balance").getAsDouble();
            lastLogin = jsonObject.get("lastLogin").getAsLong();
            lastCountActivity = jsonObject.get("lastCountActivity").getAsLong();
            if(jsonObject.has("isPlayer"))
                isPlayer = jsonObject.get("isPlayer").getAsBoolean();
            else if(GrandEconomy.getServer().getUserCache().getByUuid(uuid) != null) {
                this.isPlayer = true;
                this.changed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write() throws IOException {
        write(getFile());
    }

    private void write(File location) throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("balance", balance);
        obj.addProperty("lastLogin", lastLogin);
        obj.addProperty("lastCountActivity", lastCountActivity);
        obj.addProperty("isPlayer", isPlayer);
        try (FileWriter file = new FileWriter(location)) {
            String str = obj.toString();
            file.write(str);
        }
        changed = false;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double v) {
        if(balance != v) {
            balance = v;
            changed = true;
        }
    }

    void addBalance(double v) {
        setBalance(balance + v);
    }

    private ServerPlayerEntity getPlayerMP() {
        return GrandEconomy.getServer().getPlayerManager().getPlayer(uuid);
    }
}
