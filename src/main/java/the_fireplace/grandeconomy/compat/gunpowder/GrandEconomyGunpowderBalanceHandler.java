package the_fireplace.grandeconomy.compat.gunpowder;

import io.github.gunpowder.api.module.currency.dataholders.StoredBalance;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import the_fireplace.grandeconomy.api.CurrencyAPI;

import java.math.BigDecimal;
import java.util.UUID;

public final class GrandEconomyGunpowderBalanceHandler implements BalanceHandler {

    private final CurrencyAPI currencyAPI;

    private GrandEconomyGunpowderBalanceHandler() {
        this.currencyAPI = CurrencyAPI.getInstance();
    }

    @NotNull
    @Override
    public StoredBalance[] getBalanceTop() {
        return new StoredBalance[0];
    }

    @NotNull
    @Override
    public StoredBalance getUser(@NotNull UUID uuid) {
        return new StoredBalance(uuid, BigDecimal.valueOf(currencyAPI.getBalance(uuid, null)));
    }

    @Override
    public void modifyUser(@NotNull UUID uuid, @NotNull Function1<? super StoredBalance, StoredBalance> function1) {
        //noinspection LawOfDemeter
        currencyAPI.setBalance(
            uuid,
            function1.invoke(new StoredBalance(uuid, BigDecimal.valueOf(currencyAPI.getBalance(uuid, null)))).getBalance().doubleValue(),
            null
        );
    }

    @Override
    public void updateUser(@NotNull StoredBalance storedBalance) {
        currencyAPI.setBalance(storedBalance.getUuid(), storedBalance.getBalance().doubleValue(), null);
    }

    static class Supplier implements java.util.function.Supplier<BalanceHandler> {
        private GrandEconomyGunpowderBalanceHandler balanceHandler = null;

        @Override
        public BalanceHandler get() {
            if (balanceHandler == null) {
                balanceHandler = new GrandEconomyGunpowderBalanceHandler();
            }

            return balanceHandler;
        }
    }
}
