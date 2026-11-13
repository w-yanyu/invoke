package cn.iyanyu.invoke.core.protocol.rest;

import cn.iyanyu.invoke.api.config.InvokeDefinition;
import cn.iyanyu.invoke.api.enums.HttpMethod;
import cn.iyanyu.invoke.api.model.InvokeContext;
import cn.iyanyu.invoke.api.model.InvokeRequest;
import cn.iyanyu.invoke.api.model.InvokeResponse;
import cn.iyanyu.invoke.comm.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class RestLoadProtocolHandler extends AbstractRestProtocolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestLoadProtocolHandler.class);

    private RestTemplate loadRestTemplate = (RestTemplate) SpringUtils.getBean("load_balance_rest_template_invoke", RestTemplate.class);

    @Override
    public <RES> void invoke(InvokeContext<RES> invokeContext) {
        InvokeDefinition invokeDefinition = invokeContext.getInvokeDefinition();
        InvokeRequest invokeRequest = invokeContext.getInvokeRequest();
        InvokeResponse<RES> invokeResponse = invokeContext.getInvokeResponse();
        String targetApplicationName = invokeDefinition.getExternalSvcApplicationId();
        String serviceUrl = invokeDefinition.getServiceUrl();
        Map<String, String> requestHeader = invokeRequest.getRequestHeader();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[Service Invoke] [Rest Load] Before convert request pkg, the request body is[{}], request header is[{}]", JsonUtil.format(invokeRequest.getRequestParams()), requestHeader);
        }

        Map<String, Object> requestParams = this.getPkgConverter(invokeDefinition.getPkgConverterType()).convertRequest(invokeRequest.getRequestParams(), requestHeader);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[Service Invoke] [Rest Load] Convert request pkg success! ");
        }

        HttpMethod httpMethod = invokeDefinition.getHttpMethod();
        HttpHeaders httpHeaders = this.buildHeaders(requestHeader);
        String url = this.createUrl(targetApplicationName, serviceUrl);
        ResponseEntity result;
        if (invokeDefinition.getMockFlag()) {
            result = this.doMock(invokeDefinition);
        } else {
            try {
                if (org.springframework.http.HttpMethod.GET.name().equalsIgnoreCase(httpMethod.name())) {
                    httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
                    MultiValueMap<String, String> params = new LinkedMultiValueMap();
                    requestParams.forEach((k, v) -> {
                        params.add(k, String.valueOf(v));
                    });
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                    URI uri = builder.queryParams(params).build().encode().toUri();
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[Service Invoke] [Rest Load] Send GET request, the uri is: [{}]", uri.toString());
                    }

                    result = this.loadRestTemplate.getForEntity(uri, Object.class);
                } else {
                    HttpEntity<Object> httpEntity = new HttpEntity(requestParams, httpHeaders);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[Service Invoke] [Rest Load] Send POST request, the request headers is[{}], request body is[{}]", httpEntity.getHeaders(), httpEntity.getBody());
                    }

                    result = this.loadRestTemplate.exchange(url, org.springframework.http.HttpMethod.valueOf(httpMethod.name()), httpEntity, Object.class, new Object[0]);
                }
            } catch (ResourceAccessException var16) {
                throw ServiceInvoke.E1026(var16.getMessage(), var16);
            } catch (Exception var17) {
                throw ServiceInvoke.E1005(SystemPropertiesUtil.getApplicationName(), targetApplicationName, invokeDefinition.getExternalServiceCode(), var17.getMessage(), var17);
            }
        }

        Map<String, String> responseHeaders = result.getHeaders().toSingleValueMap();
        Object resultBody = result.getBody();
        RES responseBody = this.getPkgConverter(invokeDefinition.getPkgConverterType()).convertResponse(responseHeaders, resultBody, invokeResponse.getResponseType());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[Service Invoke] [Rest Load] After convert response pkg, the response body is[{}], response header is[{}]", JsonUtil.format(responseBody), responseHeaders);
        }

        invokeResponse.setBody(responseBody);
        invokeResponse.setHeader(responseHeaders);
    }

//
//    private ResponseEntity<Object> doMock(ServiceInvokeDefinition invokeDefinition) {
//        MockPkgLoader mockPkgLoader = (MockPkgLoader)SpringUtils.getBean(MockPkgLoader.class);
//        String mockPkg = mockPkgLoader.loadMockPkg(invokeDefinition);
//        ResponseEntity<Object> responseEntity = new ResponseEntity(mockPkg, HttpStatus.OK);
//        return responseEntity;
//    }

    @Override
    public String getSpiId() {
        return "rest";
    }
}
