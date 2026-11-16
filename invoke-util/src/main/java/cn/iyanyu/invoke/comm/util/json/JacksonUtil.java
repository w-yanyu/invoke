package cn.iyanyu.invoke.comm.util.json;

import cn.iyanyu.invoke.comm.util.json.jackson.std.BigDecimalStdSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class JacksonUtil {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper(false);
    private static final ObjectMapper DESENSITIZATION_OBJECT_MAPPER = createObjectMapper(true);

    public JacksonUtil() {
    }

    private static ObjectMapper createObjectMapper(boolean desensitization) {
        JsonFactory jsonFactory = JsonFactory.builder().build();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        SimpleModule module = new SimpleModule();
        module.addSerializer(BigDecimal.class, BigDecimalStdSerializer.instance);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        module.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
        module.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        mapper.registerModules(module, new Jdk8Module());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, false);
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static String format(Object request, boolean format) {
        try {
            return format ? OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(request) : OBJECT_MAPPER.writeValueAsString(request);
        } catch (Exception var3) {
            Exception e = var3;
            throw new RuntimeException("序列化对象[" + request + "]失败", e);
        }
    }

    public static String format(Object request) {
        return format(request, false);
    }

    public static String formatWithPretty(Object o) {
        return format(o, true);
    }

    public static byte[] formatAsBytes(Object request) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(request);
        } catch (Exception var2) {
            Exception e = var2;
            throw new RuntimeException("序列化对象[" + request + "]失败", e);
        }
    }

    public static String formatWithDesensitization(Object request, boolean format) {
        try {
            return format ? DESENSITIZATION_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(request) : DESENSITIZATION_OBJECT_MAPPER.writeValueAsString(request);
        } catch (Exception var3) {
            Exception e = var3;
            throw new RuntimeException("序列化对象[" + request + "]失败", e);
        }
    }

    public static String formatWithDesensitization(Object request) {
        return formatWithDesensitization(request, false);
    }

    public static String formatPkg(Map<String, Object> request) {
        return format(request);
    }

    public static <T> String formatEntity(T entity) {
        return format(entity);
    }

    public static <T> T parseEntity(String s, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(s, type);
        } catch (Exception var3) {
            Exception e = var3;
            throw new RuntimeException("反序列化对象[" + s + "]失败", e);
        }
    }

    public static <T> T parseEntity(String s, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(s, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException("反序列化对象[" + s + "]失败", e);
        }
    }

    public static <T> T parseEntityFromFile(File file, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(file, type);
        } catch (Exception e) {
            throw new RuntimeException("反序列化对象[" + file.getName() + "]失败", e);
        }
    }

    public static Map<String, Object> parse(String s) {
        return (Map<String, Object>)parseEntity(s, new TypeReference<Map<String, Object>>() {});
    }

    public static Map<String, Object> toMap(Object o) {
        return o instanceof String ? parse(o.toString()) : OBJECT_MAPPER.convertValue(o, new TypeReference<Map<String, Object>>() {
        });
    }

    @SuppressWarnings("unchecked")
    public static <R, T> T convert(R resource, Class<T> targetClazz) {
        return ClassUtils.isPrimitiveOrWrapper(targetClazz) ? (T) resource : OBJECT_MAPPER.convertValue(resource, targetClazz);
    }

    public static String genStringFromJsonMap(Map<?, ?> map, String key) {
        Object result = map.get(key);
        if (result == null) {
            return null;
        } else if (ClassUtils.isPrimitiveOrWrapper(result.getClass())) {
            return result.toString();
        } else {
            return result instanceof String ? (String)result : format(result);
        }
    }

    public static <T> T parseEntity(byte[] bytes, String charset, Class<T> type) {
        return parseEntity(new String(bytes, Charset.forName(charset)), type);
    }

    public static Object[] parseArgs(String argsJsons) {
        return (Object[])parseEntity(argsJsons, Object[].class);
    }
}
