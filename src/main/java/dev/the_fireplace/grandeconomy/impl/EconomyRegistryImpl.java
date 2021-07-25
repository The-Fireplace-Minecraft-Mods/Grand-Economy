package dev.the_fireplace.grandeconomy.impl;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import dev.the_fireplace.grandeconomy.api.interfaces.Economy;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class EconomyRegistryImpl implements EconomyRegistry {

    private final Map<String, Economy> econHandlers = new ConcurrentHashMap<>();

    @Override
    public boolean hasEconomyHandler(String key) {
        return econHandlers.containsKey(key);
    }

    @Override
    public Economy getEconomyHandler(String key) {
        return econHandlers.get(key);
    }

    @Override
    public Collection<String> getEconomyHandlers() {
        return econHandlers.keySet();
    }

    @Override
    public boolean registerEconomyHandler(Economy handler, String modid, String... aliases) {
        if (econHandlers.containsKey(modid))
            return false;
        econHandlers.put(modid, handler);
        for (String alias: aliases)
            econHandlers.putIfAbsent(alias, handler);
        return true;
    }
}
