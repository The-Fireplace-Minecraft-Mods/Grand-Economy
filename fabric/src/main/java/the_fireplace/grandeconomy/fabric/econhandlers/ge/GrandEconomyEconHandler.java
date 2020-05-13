package the_fireplace.grandeconomy.fabric.econhandlers.ge;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.fabric.Config;
import the_fireplace.grandeconomy.fabric.GrandEconomy;
import the_fireplace.grandeconomy.fabric.translation.TranslationUtil;
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
            return Config.currencyNameSingular;
        return Config.currencyNameMultiple;
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return amount + " " + getCurrencyName(amount);
    }

    @Override
    public String getId() {
        return GrandEconomyApi.MODID;
    }

    private static long lastTickEvent = 0;
    @Override
    public void init() {
        ServerTickCallback.EVENT.register(s -> {
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
                    playerEntity.sendMessage(TranslationUtil.getTranslation(playerEntity.getUuid(), "commands.grandeconomy.common.balance", GrandEconomyApi.formatCurrency(account.getBalance())));
            }
        });
    }
}
