package dev.the_fireplace.grandeconomy.compat.gunpowder;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.Economy;
import io.github.gunpowder.api.module.currency.dataholders.StoredBalance;
import io.github.gunpowder.api.module.currency.modelhandlers.BalanceHandler;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public final class GrandEconomyGunpowderBalanceHandler implements BalanceHandler
{
    private final Economy economy;

    private GrandEconomyGunpowderBalanceHandler() {
        this.economy = GrandEconomyConstants.getInjector().getInstance(Economy.class);
    }

    @NotNull
    @Override
    public StoredBalance[] getBalanceTop() {
        return new StoredBalance[0];
    }

    @NotNull
    @Override
    public StoredBalance getUser(@NotNull UUID uuid) {
        return new StoredBalance(uuid, BigDecimal.valueOf(economy.getBalance(uuid, null)));
    }

    @Override
    public void modifyUser(@NotNull UUID uuid, @NotNull Function1<? super StoredBalance, StoredBalance> function1) {
        economy.setBalance(
            uuid,
            function1.invoke(new StoredBalance(uuid, BigDecimal.valueOf(economy.getBalance(uuid, null)))).getBalance().doubleValue(),
            null
        );
    }

    @Override
    public void updateUser(@NotNull StoredBalance storedBalance) {
        economy.setBalance(storedBalance.getUuid(), storedBalance.getBalance().doubleValue(), null);
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
