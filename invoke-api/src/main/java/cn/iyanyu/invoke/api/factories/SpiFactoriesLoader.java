package cn.iyanyu.invoke.api.factories;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpiFactoriesLoader implements FactoriesLoader {
    private static final Map<Class<?>, Map<String, Object>> cache = new ConcurrentHashMap();
    private static final Map<Class<?>, Object> newestCache = new ConcurrentHashMap();
    private static final Map<Class<?>, Object> oldestCache = new ConcurrentHashMap();
    private static final Map<String, Object> NULL_MAP = Collections.emptyMap();

    @Override
    public <T> List<T> getAllFactories(Class<T> factoryClass) {
        Map<String, T> factoryInstances = getFactories(factoryClass);
        if (factoryInstances.size() == 0) {
            return Collections.emptyList();
        } else {
            List<T> ret = new ArrayList();
            Iterator var3 = factoryInstances.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, T> entry = (Map.Entry)var3.next();
                ret.add(entry.getValue());
            }

            return ret;
        }
    }

    private static <T> Map<String, T> getFactories(Class<T> factoryClass) {
        Map<String, T> factoryInstances = (Map)cache.get(factoryClass);
        if (factoryInstances == null) {
            loadFactories(factoryClass);
            factoryInstances = (Map)cache.get(factoryClass);
        }

        return factoryInstances;
    }

    private static void loadFactories(Class<?> factoryClass) {
        List<?> factories = null;//SpringFactoriesLoader.loadFactories(factoryClass, (ClassLoader)null);
        if (factories == null) {
            cache.put(factoryClass, NULL_MAP);
        } else {
            Map<String, Object> factoryInstances = new LinkedHashMap();

            Object factory;
            String spiName;
            for(Iterator var3 = factories.iterator(); var3.hasNext(); factoryInstances.put(spiName, factory)) {
                factory = var3.next();
                spiName = getSpiName(factory.getClass());
                Object old = factoryInstances.get(spiName);
                if (old != null) {
                    LOGGER.warn("SPI id has already exist, new class's order >= old class's order, ovrride.[new class = [%s], old class = [%s]]", factory.getClass().getName(), old.getClass().getName());
                }
            }

            if (factories.size() > 0) {
                newestCache.putIfAbsent(factoryClass, factories.get(factories.size() - 1));
                oldestCache.putIfAbsent(factoryClass, factories.get(0));
            }

            cache.putIfAbsent(factoryClass, factoryInstances);
        }
    }
}
