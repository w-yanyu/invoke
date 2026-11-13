package cn.iyanyu.invoke.comm.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FactoriesLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoriesLoader.class);
    private static final Map<Class<?>, Map<String, Object>> cache = new ConcurrentHashMap();
    private static final Map<Class<?>, Object> newestCache = new ConcurrentHashMap();
    private static final Map<Class<?>, Object> oldestCache = new ConcurrentHashMap();
    private static final Map<String, Object> NULL_MAP = Collections.emptyMap();

    private FactoriesLoader() {
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
        List<?> factories = SpringFactoriesLoader.loadFactories(factoryClass, (ClassLoader)null);
        if (factories == null) {
            cache.put(factoryClass, NULL_MAP);
        } else {
            Map<String, Object> factoryInstances = new LinkedHashMap<>();

            Object factory;
            String spiName;
            for(Iterator<?> var3 = factories.iterator(); var3.hasNext(); factoryInstances.put(spiName, factory)) {
                factory = var3.next();
                spiName = getSpiName(factory.getClass());
                Object old = factoryInstances.get(spiName);
                if (old != null) {
                    LOGGER.warn("Spi id has already exist, new class's order >= old class's order, ovrride.[new class = [%s], old class = [%s]]", factory.getClass().getName(), old.getClass().getName());
                }
            }

            if (!factories.isEmpty()) {
                newestCache.putIfAbsent(factoryClass, factories.get(factories.size() - 1));
                oldestCache.putIfAbsent(factoryClass, factories.get(0));
            }

            cache.putIfAbsent(factoryClass, factoryInstances);
        }
    }

    public static String getSpiName(Class<?> clazz) {
        SpiImplementation spiMeta = (SpiImplementation)clazz.getAnnotation(SpiImplementation.class);
        if (spiMeta == null) {
            return clazz.getSimpleName();
        } else {
            return !"".equals(spiMeta.id()) ? spiMeta.id() : clazz.getSimpleName();
        }
    }

    public static <T> T getDefaultFactory(Class<T> factoryClass) {
        SpiInterface spi = (SpiInterface)factoryClass.getAnnotation(SpiInterface.class);
        if (spi == null) {
            return null;
        } else {
            String id = spi.defaultId();
            if (id != null) {
                Map<String, T> factoryInstances = getFactories(factoryClass);
                return factoryInstances.get(id);
            } else {
                return null;
            }
        }
    }

    public static <T> T getNewestFactory(Class<T> factoryClass) {
        Map<String, T> factoryInstances = getFactories(factoryClass);
        return factoryInstances.isEmpty() ? null : (T) newestCache.get(factoryClass);
    }

    public static <T> T getOldestFactory(Class<T> factoryClass) {
        Map<String, T> factoryInstances = getFactories(factoryClass);
        return factoryInstances.isEmpty() ? null : (T) oldestCache.get(factoryClass);
    }

    public static <T> T getFactoryById(Class<T> factoryClass, String id) {
        Map<String, T> factoryInstances = getFactories(factoryClass);
        return factoryInstances.isEmpty() ? null : factoryInstances.get(id);
    }

    public static <T> List<T> getAllFactories(Class<T> factoryClass) {
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
}