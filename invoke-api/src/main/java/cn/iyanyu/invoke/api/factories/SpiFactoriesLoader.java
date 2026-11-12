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
        if (factoryInstances == null || factoryInstances.isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(factoryInstances.values());
        }
    }

    private static <T> Map<String, T> getFactories(Class<T> factoryClass) {
        Map factoryInstances = (Map<String, T>)cache.get(factoryClass);
        if (factoryInstances == null) {
            loadFactories(factoryClass);
            factoryInstances = (Map)cache.get(factoryClass);
        }

        return factoryInstances;
    }

    private static void loadFactories(Class<?> factoryClass) {
        ServiceLoader<?> factories = ServiceLoader.load(factoryClass);

        Map<String, Object> factoryInstances = new LinkedHashMap<>();
        Object oldest = null;
        Object newest = null;

        for (Object factory : factories) {
            String spiName = getSpiName(factory.getClass());
            if (spiName == null || spiName.isEmpty()) {
                continue;
            }

            Object old = factoryInstances.putIfAbsent(spiName, factory);
            if (old != null) {
                continue;
            }

            if (oldest == null) {
                oldest = factory;
            }
            newest = factory;
        }

        if (oldest != null) {
            newestCache.putIfAbsent(factoryClass, newest);
            oldestCache.putIfAbsent(factoryClass, oldest);
        }

        cache.putIfAbsent(factoryClass, factoryInstances);
    }

    public static String getSpiName(Class<?> clazz) {
        SpiMeta spiMeta = (SpiMeta)clazz.getAnnotation(SpiMeta.class);
        if (spiMeta == null) {
            return clazz.getSimpleName();
        } else {
            return !"".equals(spiMeta.id()) ? spiMeta.id() : clazz.getSimpleName();
        }
    }
}
