package cn.iyanyu.invoke.comm.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SystemPropertiesUtil {

    private static final Map<String, String> CACHE;


    static {
        DEFAULT_APS_SYSTEM_ARCH = SystemArch.ZONE;
        DEFAULT_APS_ENV_TYPE = EnvType.PROD;
        CACHE = new ConcurrentHashMap(16);
        CACHE.putAll(getProperties("aps", false));
    }

    private static Map<String, String> getProperties(String prefix, boolean filterPrefix) {
        validateKey(prefix);
        String trimPrefix = prefix.trim();
        return System.getProperties().stringPropertyNames().stream().filter((key) -> key.startsWith(trimPrefix)).collect(Collectors.toMap((key) -> filterPrefix ? key.substring(trimPrefix.length()) : key, System::getProperty));
    }

    public static String getProperty(String key, String defaultValue) {
        validateKey(key);
        return CACHE.computeIfAbsent(key, (k) -> System.getProperty(k, defaultValue));
    }

    private static void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
    }
}
