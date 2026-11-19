package cn.iyanyu.invoke.comm.util.json;

import cn.iyanyu.base.comm.factories.FactoriesLoader;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static final JsonService jsonService = (JsonService) FactoriesLoader.getFactoryById(JsonService.class, System.getProperty("app.util.json.type", "fastjson"));

    public JsonUtil() {
    }

    public static String format(Object data) {
        return jsonService.format(data);
    }

    public static String formatWithPretty(Object data) {
        return jsonService.format(data, true);
    }

    public static String formatPkg(Object data) {
        return jsonService.formatPkg(data);
    }

    public static byte[] format(Object data, String charset) {
        return jsonService.format(data, charset);
    }

    public static <T> T parse(String str, Class<T> clazz) {
        return jsonService.parse(str, clazz);
    }

    public static <T> T parse(String str, Type type) {
        return jsonService.parse(str, type);
    }

    public static Map<String, Object> parse(String str) {
        return jsonService.parseMap(str);
    }

    public static <T> List<T> parseList(String str, Type type) {
        return jsonService.parseList(str, type);
    }

    public static <T> List<T> parseList(String str, Class<T> clazz) {
        return jsonService.parseList(str, clazz);
    }

    public static <T> T[] parseArgs(String str, Class<T> clazz) {
        return jsonService.parseArgs(str, clazz);
    }

}
