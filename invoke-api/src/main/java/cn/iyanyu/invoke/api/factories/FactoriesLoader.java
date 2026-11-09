package cn.iyanyu.invoke.api.factories;

import java.util.List;

public interface FactoriesLoader {

    <T>List<T> getAllFactories(Class<T> factoryClass);
}
