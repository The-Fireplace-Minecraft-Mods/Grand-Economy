package the_fireplace.grandeconomy.econhandlers.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.econhandlers.IEconHandler;

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
    public long getBalance(UUID uuid) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().getBalance(getEcon().getDefaultCurrency()).longValue();
        return 0;
    }

    @Override
    public boolean addToBalance(UUID uuid, long amount, boolean showMsg) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().deposit(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.economy)).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public boolean takeFromBalance(UUID uuid, long amount, boolean showMsg) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().withdraw(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.economy)).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public boolean setBalance(UUID uuid, long amount, boolean showMsg) {
        if(getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent())
            return getEcon().getOrCreateAccount(uuid).get().setBalance(getEcon().getDefaultCurrency(), BigDecimal.valueOf(amount), Cause.of(EventContext.empty(), GrandEconomy.economy)).getResult().equals(ResultType.SUCCESS);
        return false;
    }

    @Override
    public String getCurrencyName(long amount) {
        return amount == 1 ? getEcon().getDefaultCurrency().getDisplayName().toPlain() : getEcon().getDefaultCurrency().getPluralDisplayName().toPlain();
    }

    @Override
    public boolean ensureAccountExists(UUID uuid) {
        return getEcon() != null && getEcon().getOrCreateAccount(uuid).isPresent();
    }

    @Override
    public Boolean forceSave(UUID uuid) {
        return null;
    }

    @Override
    public void init() {

    }
}
