package cn.iyanyu.invoke.comm.util.json.jackson.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalStdSerializer extends StdSerializer<BigDecimal> {
    public static final BigDecimalStdSerializer instance = new BigDecimalStdSerializer(BigDecimal.class);

    protected BigDecimalStdSerializer(Class<BigDecimal> t) {
        super(t);
    }

    protected BigDecimalStdSerializer(JavaType type) {
        super(type);
    }

    protected BigDecimalStdSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected BigDecimalStdSerializer(StdSerializer<?> src) {
        super(src);
    }

    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            if (ProfileSwitcher.get().enableJsonExNum) {
                gen.writeNumber(value);
            } else if (ProfileSwitcher.get().decimalToString) {
                gen.writeString(value.toPlainString());
            } else {
                gen.writeString(value.toEngineeringString());
            }

        }
    }
}