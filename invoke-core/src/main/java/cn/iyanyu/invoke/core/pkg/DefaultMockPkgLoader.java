package cn.iyanyu.invoke.core.pkg;

import cn.iyanyu.invoke.api.config.InvokeDefinition;
import cn.iyanyu.invoke.api.pkg.MockPkgLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DefaultMockPkgLoader implements MockPkgLoader {
    @Override
    public String loadMockPkg(InvokeDefinition definition) {
        String internalServiceCode = definition.getInternalServiceCode();
        String resourcePath = "mock/" + internalServiceCode + ".txt";

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = InvokeDefinition.class.getClassLoader();
        }

        try (InputStream is = cl.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException(
                        "Mock pkg not found on classpath: resourcePath=[" + resourcePath
                                + "], internalServiceCode=[" + internalServiceCode + "]");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to read mock pkg: resourcePath=[" + resourcePath
                            + "], internalServiceCode=[" + internalServiceCode + "]", e);
        }
    }
}
