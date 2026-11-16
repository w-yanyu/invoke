package cn.iyanyu.invoke.comm.bean.model;

import cn.iyanyu.invoke.comm.factories.FactoriesLoader;

public class ModelObjectUtil {
    private ModelObjectUtil() {
    }

    public static ModelObjectCreator getModelObjectCreator() {
        return (ModelObjectCreator) FactoriesLoader.getNewestFactory(ModelObjectCreator.class);
    }
}