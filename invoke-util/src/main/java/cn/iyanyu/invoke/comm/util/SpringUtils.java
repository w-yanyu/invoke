package cn.iyanyu.invoke.comm.util;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SpringUtils implements ApplicationContextInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);
    public static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrap";
    private static ApplicationContext applicationContext;
    private static final Map<String, String> CACHE = new ConcurrentHashMap(16);

    public SpringUtils() {
    }

    public static Environment getEnv() {
        return (Environment)getApplicationContext().getBean(Environment.class);
    }

    public void initialize(ConfigurableApplicationContext context) {
        if (applicationContext == null && !context.getEnvironment().getPropertySources().contains("bootstrap")) {
            applicationContext = context;
        }

    }

    public static Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T getBeanWithLog(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception var2) {
            LOGGER.error(var2.getMessage());
            return null;
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, (String)null);
    }

    public static String getProperty(String key, String defaultValue) {
        validateKey(key);
        return (String)CACHE.computeIfAbsent(key, (value) -> {
            return getEnv().getProperty(key, defaultValue);
        });
    }

    private static void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key must not be empty");
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String decapitalize(String s) {
        if (s != null && s.length() != 0) {
            if (s.length() > 1 && Character.isUpperCase(s.charAt(1)) && Character.isUpperCase(s.charAt(0))) {
                return s;
            } else {
                char[] ac = s.toCharArray();
                ac[0] = Character.toLowerCase(ac[0]);
                return new String(ac);
            }
        } else {
            return s;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static Set<String> getBasePackages(boolean includeFramework) {
        Set<String> basePackages = new HashSet();
        if (includeFramework) {
            basePackages.add("cn.sunline.aps");
        }

        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory)getApplicationContext().getAutowireCapableBeanFactory();
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(SpringBootApplication.class);
        Assert.notNull(beansWithAnnotation, "please check the boot app annotation");
        beansWithAnnotation.forEach((key, value) -> {
            SpringBootApplication annotation = (SpringBootApplication) AnnotationUtils.findAnnotation(value.getClass(), SpringBootApplication.class);
            if (annotation != null) {
                String[] pkgArr = annotation.scanBasePackages();
                String[] var5 = pkgArr;
                int var6 = pkgArr.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String pkg = var5[var7];
                    if (StringUtils.isNotBlank(pkg)) {
                        boolean skip = false;
                        Iterator<String> it = basePackages.iterator();

                        while(it.hasNext()) {
                            String old = (String)it.next();
                            if (old.startsWith(pkg)) {
                                it.remove();
                            } else if (pkg.startsWith(old)) {
                                skip = true;
                                break;
                            }
                        }

                        if (!skip) {
                            basePackages.add(pkg);
                        }
                    }
                }
            } else {
                LOGGER.warn("Boot app bean does not have SpringBootApplication annotation");
            }

        });
        return basePackages;
    }
}