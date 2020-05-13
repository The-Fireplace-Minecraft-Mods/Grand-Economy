package the_fireplace.grandeconomy.forge.compat.sponge;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import mcp.MethodsReturnNonnullByDefault;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransferResult;
import org.spongepowered.api.text.Text;
import the_fireplace.grandeconomy.api.GrandEconomyApi;
import the_fireplace.grandeconomy.forge.Config;
import the_fireplace.grandeconomy.forge.GrandEconomy;
import the_fireplace.grandeconomy.forge.compat.IRegisterable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RegisterSpongeEconomy implements EconomyService, IRegisterable {

    private final TransactionType genericTransactionType = new TransactionType() {
        @Override
        public String getId() {
            return GrandEconomyApi.MODID+"-generic";
        }

        @Override
        public String getName() {
            return "Generic";
        }
    };

    private final Currency geCurrency = new Currency() {

        @Override
        public String getId() {
            return GrandEconomyApi.MODID+"-api";
        }

        @Override
        public String getName() {
            return getDisplayName().toPlain();
        }

        @Override
        public Text getDisplayName() {
            return Text.of(GrandEconomyApi.getCurrencyName(1));
        }

        @Override
        public Text getPluralDisplayName() {
            return Text.of(GrandEconomyApi.getCurrencyName(2));
        }

        @Override
        public Text getSymbol() {
            return Text.EMPTY;
        }

        @Override
        public Text format(BigDecimal amount, int numFractionDigits) {
            return Text.of(GrandEconomyApi.formatCurrency(amount.doubleValue()));
        }

        @Override
        public int getDefaultFractionDigits() {
            return 0;
        }

        @Override
        public boolean isDefault() {
            return true;
        }
    };

    @Override
    public Currency getDefaultCurrency() {
        return geCurrency;
    }

    @Override
    public Set<Currency> getCurrencies() {
        return Sets.newHashSet(geCurrency);
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return true;
    }

    @Override
    public boolean hasAccount(String identifier) {
        return true;
    }

    private final HashMap<UUID, UniqueAccount> accts = Maps.newHashMap();

    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        if(!accts.containsKey(uuid))
            accts.put(uuid, new UniqueAccount() {
                @Override
                public Text getDisplayName() {
                    GameProfile profile = GrandEconomy.getServer().getPlayerProfileCache().getProfileByUUID(uuid);
                    return Text.of(profile != null ? profile.getName() : uuid.toString());
                }

                @Override
                public BigDecimal getDefaultBalance(Currency currency) {
                    return BigDecimal.valueOf(Config.startBalance);
                }

                @Override
                public boolean hasBalance(Currency currency, Set<Context> contexts) {
                    return GrandEconomyApi.getBalance(getUniqueId(), null) > 0;
                }

                @Override
                public BigDecimal getBalance(Currency currency, Set<Context> contexts) {
                    return BigDecimal.valueOf(GrandEconomyApi.getBalance(getUniqueId(), null));
                }

                @Override
                public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) {
                    return Maps.asMap(Collections.singleton(geCurrency), x -> getBalance(x, contexts));
                }

                @Override
                public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
                    return new TransactionResult() {
                        @Override
                        public Account getAccount() {
                            return accts.get(getUniqueId());
                        }

                        @Override
                        public Currency getCurrency() {
                            return geCurrency;
                        }

                        @Override
                        public BigDecimal getAmount() {
                            return amount;
                        }

                        @Override
                        public Set<Context> getContexts() {
                            return contexts;
                        }

                        @Override
                        public ResultType getResult() {
                            return GrandEconomyApi.setBalance(getUniqueId(), amount.doubleValue(), null) ? ResultType.SUCCESS : ResultType.FAILED;
                        }

                        @Override
                        public TransactionType getType() {
                            return genericTransactionType;
                        }
                    };
                }

                @Override
                public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) {
                    return Maps.asMap(Collections.singleton(geCurrency), x -> resetBalance(x, cause));
                }

                @Override
                public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) {
                    GrandEconomyApi.setBalance(uuid, Config.startBalance, null);
                    return new TransactionResult() {
                        @Override
                        public Account getAccount() {
                            return accts.get(uuid);
                        }

                        @Override
                        public Currency getCurrency() {
                            return geCurrency;
                        }

                        @Override
                        public BigDecimal getAmount() {
                            return BigDecimal.ZERO;
                        }

                        @Override
                        public Set<Context> getContexts() {
                            return contexts;
                        }

                        @Override
                        public ResultType getResult() {
                            return ResultType.SUCCESS;
                        }

                        @Override
                        public TransactionType getType() {
                            return genericTransactionType;
                        }
                    };
                }

                @Override
                public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
                    return new TransactionResult() {
                        @Override
                        public Account getAccount() {
                            return accts.get(getUniqueId());
                        }

                        @Override
                        public Currency getCurrency() {
                            return geCurrency;
                        }

                        @Override
                        public BigDecimal getAmount() {
                            return amount;
                        }

                        @Override
                        public Set<Context> getContexts() {
                            return contexts;
                        }

                        @Override
                        public ResultType getResult() {
                            GrandEconomyApi.addToBalance(getUniqueId(), amount.doubleValue(), null);
                            return ResultType.SUCCESS;
                        }

                        @Override
                        public TransactionType getType() {
                            return genericTransactionType;
                        }
                    };
                }

                @Override
                public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
                    return new TransactionResult() {
                        @Override
                        public Account getAccount() {
                            return accts.get(getUniqueId());
                        }

                        @Override
                        public Currency getCurrency() {
                            return geCurrency;
                        }

                        @Override
                        public BigDecimal getAmount() {
                            return amount;
                        }

                        @Override
                        public Set<Context> getContexts() {
                            return contexts;
                        }

                        @Override
                        public ResultType getResult() {
                            return GrandEconomyApi.takeFromBalance(getUniqueId(), amount.doubleValue(), null) ? ResultType.SUCCESS : ResultType.FAILED;
                        }

                        @Override
                        public TransactionType getType() {
                            return genericTransactionType;
                        }
                    };
                }

                @Override
                public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
                    return new TransferResult() {
                        @Override
                        public Account getAccountTo() {
                            return to;
                        }

                        @Override
                        public Account getAccount() {
                            return accts.get(getUniqueId());
                        }

                        @Override
                        public Currency getCurrency() {
                            return geCurrency;
                        }

                        @Override
                        public BigDecimal getAmount() {
                            return amount;
                        }

                        @Override
                        public Set<Context> getContexts() {
                            return contexts;
                        }

                        @Override
                        public ResultType getResult() {
                            boolean taken = GrandEconomyApi.takeFromBalance(getUniqueId(), amount.doubleValue(), null);
                            if(taken)
                                GrandEconomyApi.addToBalance(UUID.fromString(to.getIdentifier()), amount.doubleValue(), null);
                            return taken ? ResultType.SUCCESS : ResultType.FAILED;
                        }

                        @Override
                        public TransactionType getType() {
                            return genericTransactionType;
                        }
                    };
                }

                @Override
                public String getIdentifier() {
                    return getUniqueId().toString();
                }

                @Override
                public Set<Context> getActiveContexts() {
                    return Collections.singleton(new Context(Context.USER_KEY, "user"));
                }

                @Override
                public UUID getUniqueId() {
                    return uuid;
                }
            });
        return Optional.of(accts.get(uuid));
    }

    @Override
    public Optional<Account> getOrCreateAccount(String identifier) {
        try {
            UUID uuid = UUID.fromString(identifier);
            Optional<UniqueAccount> opt = getOrCreateAccount(uuid);
            //noinspection OptionalIsPresent
            if(opt.isPresent())
                return Optional.of(opt.get());
            return Optional.empty();
        } catch(IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Account> calculator) {

    }

    @Override
    public void register() {
        Sponge.getServiceManager().setProvider(GrandEconomy.instance, EconomyService.class, this);
    }
}
