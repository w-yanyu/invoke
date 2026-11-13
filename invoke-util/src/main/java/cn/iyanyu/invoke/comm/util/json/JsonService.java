package cn.iyanyu.invoke.comm.util.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JsonService {
    String format(Object data);

    String format(Object data, boolean isPretty);

    String formatPkg(Object data);

    byte[] format(Object data, String charset);

    <T> T parse(String str, Class<T> clazz);

    <T> T parse(String str, Type type);

    Map<String, Object> parseMap(String str);

    <T> List<T> parseList(String str, Type type);

    default <T> List<T> parseList(String str, Class<T> clazz) {
        return this.parseList(str, (Type)clazz);
    }

    <T> T[] parseArgs(String str, Type type);

    default <T> T[] parseArgs(String str, Class<T> clazz) {
        return this.parseArgs(str, (Type)clazz);
    }
}
