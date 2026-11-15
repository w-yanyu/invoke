package cn.iyanyu.invoke.api.pkg;

import java.util.Map;

public interface InvokePkgConverter {
    String DEFAULT_CONVERTER_BEAN_NAME = "json";

    Map<String, Object> convertRequest(Map<String, Object> requestParams, Map<String, String> requestHeaders);

    <RES> RES convertResponse(Map<String, String> responseHeaders, Object responseBody, Class<? extends RES> expectedResponseType);
}