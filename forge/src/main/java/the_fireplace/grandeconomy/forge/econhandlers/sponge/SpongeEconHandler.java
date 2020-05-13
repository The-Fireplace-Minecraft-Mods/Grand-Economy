package the_fireplace.grandeconomy.forge.econhandlers.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import the_fireplace.grandeconomy.api.IEconHandler;
import the_fireplace.grandeconomy.forge.GrandEconomy;

import java.math.BigDecimal;
import java.util.UUID;

public class SpongeEconHandler implements IEconHandler {

    private EconomyService spongeEcon;

    private EconomyService getEcon() {
        if(spongeEcon == null)
            spongeEcon = Sponge.getServiceManager().provide(EconomyService.class).isPresent() ? Sponge.getServiceManager().provide(EconomyService.class).get() : null;
        return spongeEcon;
    }

    @Override
    public double getBalance(UUID uuid, Boolean isPlayer) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().getBalance(getEcon().getDefaultCurrency()).doubleValue();
        return 0;
    }

    @Override
    public boolean addToBalance(UUID uuid, double amount, Boolean isPlayer) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().deposit(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.getEconomy())).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, double amount, Boolean isPlayer) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().withdraw(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.getEconomy())).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public boolean setBalance(UUID uuid, double amount, Boolean isPlayer) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().setBalance(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.getEconomy())).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public String getCurrencyName(double amount) {
        return amount == 1 ? getEcon().getDefaultCurrency().getDisplayName().toPlain() : getEcon().getDefaultCurrency().getPluralDisplayName().toPlain();
    }

    @Override
    public String getFormattedCurrency(double amount) {
        return getEcon().getDefaultCurrency().format(BigDecimal.valueOf(amount)).toPlain();
    }

    @Override
    public String getId() {
        return Sponge.getServiceManager().getRegistration(EconomyService.class).isPresent() ? Sponge.getServiceManager().getRegistration(EconomyService.class).get().getPlugin().getId() : "spongeapi";
    }

    @Override
    public void init() {

    }
}
