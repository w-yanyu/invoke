package cn.iyanyu.invoke.comm.util.json.jackson;

import cn.iyanyu.invoke.comm.util.json.JsonService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class JacksonService implements JsonService {
    public static final String JSON_TYPE = "jackson";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper(JsonFactory.builder().build());
    private static ObjectMapper PKG_MAPPER;

    public JacksonService() {
    }

    @Override
    public String format(Object data) {
        return this.format(data, false);
    }

    @Override
    public String format(Object data, boolean isPretty) {
        try {
            return isPretty ? OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data) : OBJECT_MAPPER.writeValueAsString(data);
        } catch (Exception var4) {
            throw new RuntimeException(SP_CE.E005_ERROR() + data + SP_CE.E007_ERROR(), var4);
        }
    }

    @Override
    public String formatPkg(Object data) {
        try {
            return PKG_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(SP_CE.E005_ERROR() + data + SP_CE.E007_ERROR(), var3);
        }
    }

    @Override
    public byte[] format(Object data, String charset) {
        try {
            return this.format(data, false).getBytes(charset);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public <T> T parse(String str, Class<T> clazz) {
        return this.parse(str, (Type)clazz);
    }

    @Override
    public <T> T parse(String str, Type type) {
        try {
            return OBJECT_MAPPER.readValue(str, OBJECT_MAPPER.constructType(type));
        } catch (Exception var4) {
            throw new RuntimeException(SP_CE.E006_ERROR() + str + SP_CE.E007_ERROR(), var4);
        }
    }

    @Override
    public Map<String, Object> parseMap(String str) {
        return (Map)this.parse(str, (new TypeReference<Map<String, Object>>() {
        }).getType());
    }

    @Override
    public <T> List<T> parseList(String str, Type type) {
        try {
            JavaType javaType = OBJECT_MAPPER.constructType(type);
            return (List)OBJECT_MAPPER.readValue(str, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, javaType));
        } catch (JsonProcessingException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public <T> T[] parseArgs(String str, Type type) {
        try {
            JavaType elementType = OBJECT_MAPPER.constructType(type);
            return (T[]) OBJECT_MAPPER.readValue(str, OBJECT_MAPPER.getTypeFactory().constructArrayType(elementType));
        } catch (JsonProcessingException var4) {
            throw new RuntimeException(var4);
        }
    }

    static {
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
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        OBJECT_MAPPER.registerModules(new Module[]{module, new Jdk8Module(), new ApsModule(true)});
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        PKG_MAPPER = new ObjectMapper(JsonFactory.builder().build());
        SimpleModule pkgModule = new SimpleModule();
        pkgModule.addSerializer(BigDecimal.class, BigDecimalStdSerializer.instance);
        pkgModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        pkgModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        pkgModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        pkgModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        pkgModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        pkgModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        pkgModule.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
        pkgModule.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        pkgModule.addSerializer(Boolean.class, ToStringSerializer.instance);
        pkgModule.addSerializer(Boolean.TYPE, ToStringSerializer.instance);
        PKG_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        PKG_MAPPER.registerModules(new Module[]{pkgModule, new Jdk8Module(), new ApsModule(true)});
        PKG_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PKG_MAPPER.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        PKG_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        PKG_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        PKG_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        PKG_MAPPER.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
    }
}