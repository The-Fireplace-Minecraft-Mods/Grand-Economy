package the_fireplace.grandeconomy.logintracker;

import com.google.gson.JsonObject;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.io.AccountData;
import the_fireplace.grandeconomy.time.CurrentDay;
import the_fireplace.lib.api.io.JsonObjectReader;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LoginTracker extends AccountData {
    private static final Map<UUID, LoginTracker> PLAYER_TIME_TRACKER_INSTANCES = new ConcurrentHashMap<>();

    public static LoginTracker get(UUID accountId) {
        return PLAYER_TIME_TRACKER_INSTANCES.computeIfAbsent(accountId, LoginTracker::new);
    }

    public static void delete(UUID accountId) {
        LoginTracker account = PLAYER_TIME_TRACKER_INSTANCES.remove(accountId);
        if (account != null) {
            account.deleteSaveFile();
        }
    }

    private long lastLogin = CurrentDay.getCurrentDay();

    private LoginTracker(UUID uuid) {
        super(uuid, "login");
        if (!loadSavedData()) {
            markChanged();
        }
    }

    public void addLogin() {
        long now = CurrentDay.getCurrentDay();
        long daysSinceLastLogin = now - this.lastLogin;
        this.lastLogin = now;

        if (daysSinceLastLogin == 0) {
            return;
        }

        distributeBasicIncome(daysSinceLastLogin);
    }

    private void distributeBasicIncome(long days) {
        if (GrandEconomy.config.basicIncome) {
            if (days > GrandEconomy.config.maxIncomeSavingsDays)
                days = GrandEconomy.config.maxIncomeSavingsDays;
            GrandEconomyApi.addToBalance(getId(), days * GrandEconomy.config.basicIncomeAmount, true);
        }
    }

    @Override
    public void readFromJson(JsonObjectReader reader) {
        lastLogin = reader.readLong("lastLogin", lastLogin);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("lastLogin", lastLogin);
        return obj;
    }
}
