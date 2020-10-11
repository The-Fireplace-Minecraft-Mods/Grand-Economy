package the_fireplace.grandeconomy.ge;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.config.ModConfig;
import the_fireplace.grandeconomy.translation.TranslationUtil;
import the_fireplace.grandeconomy.utils.TimeUtils;

import java.util.UUID;

public class GrandEconomyEconHandler implements IEconHandler {
    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return 0;
        }
        account.update();
        return account.getBalance();
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if(account == null) {
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if(account.getBalance() + amount < 0)
            return false;
        account.addBalance(amount);
        return true;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if (account.getBalance() < amount)
            return false;
        account.addBalance(-amount);
        return true;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        Account account = Account.get(uuid);
        if (account == null){
            GrandEconomy.LOGGER.warn("Account for {} was null", uuid.toString());
            return false;
        }
        if(amount < 0)
            return false;

        account.setBalance(amount);
        return true;
    }

    @Override
    public String getCurrencyName(double amount) {
        if (amount == 1)
            return ModConfig.currencyNameSingular;
        return ModConfig.currencyNameMultiple;
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomy.MODID;
    }

    private static long lastTickEvent = 0;
    @Override
    public void init() {
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            //TODO rewrite this, it can probably be made much more efficient
            long now = TimeUtils.getCurrentDay();
            if (lastTickEvent == now)
                return;
            lastTickEvent = now;
            MinecraftServer server = GrandEconomy.getServer();
            if (server == null)
                return;
            for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
                Account account = Account.get(playerEntity);
                if (account.update())
                    playerEntity.sendMessage(TranslationUtil.getTranslation(playerEntity.getUuid(), "commands.grandeconomy.common.balance", GrandEconomyApi.formatCurrency(account.getBalance())), false);
            }
        });
    }
}
