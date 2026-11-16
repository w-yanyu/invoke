package cn.iyanyu.invoke.comm.bean.model;

public interface ModelObjectCreator {
    <T> T create(Class<T> t);

    <T> T createPageObj(Class<T> t);

    <T> Class<T> resolveInterface(Class<T> type);

    <T> T createPageModel(Class<T> t);

    <T> void setModelReadonly(T t);

    <T> boolean isModelReadOnly(T t);

    <T> T cloneModel(T t);

    <T> T cloneModel(T t, boolean fullCopy);
}