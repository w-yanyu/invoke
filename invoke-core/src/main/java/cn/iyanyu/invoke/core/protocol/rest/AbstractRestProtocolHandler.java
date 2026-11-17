package cn.iyanyu.invoke.core.protocol.rest;

import cn.iyanyu.invoke.api.pkg.InvokePkgConverter;
import cn.iyanyu.invoke.api.protocol.InvokeProtocolHandler;
import cn.iyanyu.invoke.comm.util.SpringUtils;
import cn.iyanyu.invoke.error.exception.support.AppOnlError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractRestProtocolHandler implements InvokeProtocolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestProtocolHandler.class);
    Map<String, InvokePkgConverter> converterTypeMap = SpringUtils.getBeansOfType(InvokePkgConverter.class);

    public AbstractRestProtocolHandler() {
    }

    @Override
    public InvokePkgConverter getPkgConverter(String converterType) {
        InvokePkgConverter converter = (InvokePkgConverter) this.converterTypeMap.get(converterType);
        if (converter == null) {
            throw AppOnlError.InvokeError.E0001(converterType);
        } else {
            return converter;
        }
    }

    public String createUrl(String targetApplicationName, String serviceUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(targetApplicationName);
        if (!serviceUrl.startsWith("/")) {
            stringBuilder.append("/");
        }

        stringBuilder.append(serviceUrl);
        return stringBuilder.toString();
    }

    public HttpHeaders buildHeaders(Map<String, String> requestHeader) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Objects.requireNonNull(httpHeaders);
        requestHeader.forEach(httpHeaders::add);
        return httpHeaders;
    }
}
