package the_fireplace.grandeconomy.nativeeconomy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.utils.TimeUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Account {
    private static final Map<UUID, Account> ACCOUNT_INSTANCES = new ConcurrentHashMap<>();
    private static File location;
    private boolean isChanged;

    private final UUID uuid;
    private double balance;
    private long lastLogin;
    private long lastCountActivity;
    private boolean isPlayer = false;

    private Account(UUID uuid) {
        this.uuid = uuid;
        this.balance = GrandEconomy.config.startBalance;
        long now = TimeUtils.getCurrentDay();
        this.lastLogin = now;
        this.lastCountActivity = now;
        this.isChanged = true;
        if(GrandEconomy.getServer().getUserCache().getByUuid(uuid) != null)
            this.isPlayer = true;
    }

    public static Account get(PlayerEntity player) {
        return get(player.getUuid());
    }

    public static Account get(UUID uuid) {
        Account account = ACCOUNT_INSTANCES.get(uuid);
        if (account != null)
            return account;
        //noinspection ResultOfMethodCallIgnored
        location.mkdirs();

        account = new Account(uuid);
        ACCOUNT_INSTANCES.put(uuid, account);

        File file = account.getFile();
        if (!file.exists()) return account;

        account.read();
        return account;
    }

    boolean update() {
        long now = TimeUtils.getCurrentDay();
        long activityDeltaDays = now - this.lastCountActivity;
        this.lastCountActivity = now;

        if (!isPlayer || activityDeltaDays == 0) return false;

        if (GrandEconomy.config.basicIncome && getPlayerMP() != null) {
            long loginDeltaDays = (now - this.lastLogin);
            if (loginDeltaDays > GrandEconomy.config.maxIncomeSavingsDays)
                loginDeltaDays = GrandEconomy.config.maxIncomeSavingsDays;
            this.lastLogin = now;
            this.balance += loginDeltaDays * GrandEconomy.config.basicIncomeAmount;
        }
        return activityDeltaDays > 0;
    }

    void writeIfChanged() throws IOException {
        if (isChanged) write();
    }

    private File getFile() {
        return new File(location, uuid + ".json");
    }

    private void read() {
        read(getFile());
    }

    private void read(File file) {
        isChanged = false;

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
                this.isChanged = true;
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
        isChanged = false;
    }

    double getBalance() {
        return balance;
    }

    void setBalance(double v) {
        if(balance != v) {
            balance = v;
            isChanged = true;
        }
    }

    void addBalance(double v) {
        setBalance(balance + v);
    }

    @Nullable
    private ServerPlayerEntity getPlayerMP() {
        return GrandEconomy.getServer().getPlayerManager().getPlayer(uuid);
    }
}
