package cn.iyanyu.invoke.api.factories;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Factories implements FactoriesLoader {
    private static final Factories instance = new Factories();

    private FactoriesLoader factoriesLoader = new SpiFactoriesLoader();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private Factories() {
    }

    public static Factories get() {
        return instance;
    }

    public void setFactoriesLoader(FactoriesLoader factoriesLoader) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("FactoriesLoader already initialized: " + this.factoriesLoader.getClass().getName());
        }
        this.factoriesLoader = factoriesLoader;
    }

    @Override
    public <T> List<T> getAllFactories(Class<T> factoryClass) {
        return factoriesLoader.getAllFactories(factoryClass);
    }
}
