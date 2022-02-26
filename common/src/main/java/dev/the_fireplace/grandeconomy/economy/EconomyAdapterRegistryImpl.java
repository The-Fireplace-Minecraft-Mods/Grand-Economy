package dev.the_fireplace.grandeconomy.economy;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapter;
import dev.the_fireplace.grandeconomy.api.interfaces.EconomyAdapterRegistry;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class EconomyAdapterRegistryImpl implements EconomyAdapterRegistry
{
    private final Map<String, EconomyAdapter> econHandlers = new ConcurrentHashMap<>();

    public boolean hasEconomyHandler(String key) {
        return econHandlers.containsKey(key);
    }

    public EconomyAdapter getEconomyHandler(String key) {
        return econHandlers.get(key);
    }

    public Collection<String> getEconomyHandlers() {
        return econHandlers.keySet();
    }

    @Override
    public boolean registerEconomyHandler(EconomyAdapter handler, String modid, String... aliases) {
        if (econHandlers.containsKey(modid)) {
            GrandEconomyConstants.getLogger().warn("Unable to register handler for modid {} because one already exists.", modid);
            return false;
        }
        econHandlers.put(modid, handler);
        for (String alias : aliases) {
            econHandlers.putIfAbsent(alias, handler);
        }
        return true;
    }
}
