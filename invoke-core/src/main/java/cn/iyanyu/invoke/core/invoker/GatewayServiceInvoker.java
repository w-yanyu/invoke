package cn.iyanyu.invoke.core.invoker;

import cn.iyanyu.invoke.api.config.InvokeDefinition;
import cn.iyanyu.invoke.api.constants.ResponseStatus;
import cn.iyanyu.invoke.api.invoker.ServiceInvoker;
import cn.iyanyu.invoke.api.model.InvokeContext;
import cn.iyanyu.invoke.api.model.InvokeResponse;
import cn.iyanyu.invoke.api.protocol.InvokeProtocolHandler;
import cn.iyanyu.invoke.core.protocol.InvokeProtocolHandlerManager;
import io.micrometer.common.util.StringUtils;

import java.util.Map;

public class GatewayServiceInvoker implements ServiceInvoker {

    public GatewayServiceInvoker() {
    }

    @Override
    public <RES> void invoke(InvokeContext<RES> invokeContext) {
        this.initHeaders(invokeContext);
        this.initServiceUrl(invokeContext);
        this.sign(invokeContext);
        this.encrypt(invokeContext);
        String protocol = invokeContext.getInvokeDefinition().getComProtocolId();
        InvokeProtocolHandler protocolHandler = InvokeProtocolHandlerManager.get(protocol);
        protocolHandler.invoke(invokeContext);
        this.resolveResponse(invokeContext);
        this.decode(invokeContext);
        this.verify(invokeContext);
    }

    private <RES> void initHeaders(InvokeContext<RES> invokeContext) {
        Map<String, String> headers = invokeContext.getInvokeRequest().getRequestHeader();
        InvokeDefinition definition = invokeContext.getInvokeDefinition();
        if (!headers.containsKey("Connection")) {
            headers.put("Connection", "Keep-Alive");
        }

        headers.putIfAbsent("Content-Type", "application/json");
    }

    private <RES> void initServiceUrl(InvokeContext<RES> invokeContext) {
        InvokeDefinition definition = invokeContext.getInvokeDefinition();
        if (!org.springframework.util.StringUtils.hasLength(definition.getServiceUrl())) {
            definition.setServiceUrl("/gateway");
        }
    }

    private <RES> void resolveResponse(InvokeContext<RES> invokeContext) {
        InvokeResponse<RES> invokeResponse = invokeContext.getInvokeResponse();
        Map<String, String> headers = invokeResponse.getHeader();
        String responseCode = (String) headers.get("code");
        String errorMsg = "";
        ResponseStatus responseStatus;
        if (StringUtils.isBlank(responseCode)) {
            responseStatus = ResponseStatus.UNKNOWN;
        } else if ("000000000000".equals(responseCode)) {
            responseStatus = ResponseStatus.SUCCESS;
        } else {
            responseStatus = ResponseStatus.FAIL;
            errorMsg = (String) headers.get("desc");
            invokeResponse.setErrorMsg(errorMsg);
        }

        invokeResponse.setStatus(responseStatus);
    }

    private void sign(InvokeContext invokeContext) {
    }

    private void encrypt(InvokeContext invokeContext) {
    }

    private <RES> void decode(InvokeContext<RES> invokeContext) {
    }

    private <RES> void verify(InvokeContext<RES> requestMsg) {
    }
}
