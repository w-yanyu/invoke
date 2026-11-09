package cn.iyanyu.invoke.api.model;

import cn.iyanyu.invoke.api.config.InvokeDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InvokeContext<RES> implements Serializable {
    private static final long serialVersionUID = 1L;
    private InvokeRequest invokeRequest;
    private InvokeResponse<RES> invokeResponse;
    private Map<String, Object> tempData = new HashMap<>();
    private Throwable throwable;

    private InvokeDefinition invokeDefinition;

    public InvokeContext() {
    }

    public InvokeContext(InvokeRequest invokeRequest, InvokeResponse<RES> invokeResponse) {
        this.invokeRequest = invokeRequest;
        this.invokeResponse = invokeResponse;
        String internalServiceCode = invokeRequest.getInternalServiceCode();
        String callId = invokeRequest.getCallId();
    }

    public InvokeRequest getInvokeRequest() {
        return this.invokeRequest;
    }

    public void setInvokeRequest(InvokeRequest invokeRequest) {
        this.invokeRequest = invokeRequest;
    }

    public InvokeResponse<RES> getInvokeResponse() {
        return this.invokeResponse;
    }

    public void setInvokeResponse(InvokeResponse<RES> invokeResponse) {
        this.invokeResponse = invokeResponse;
    }

    public Map<String, Object> getTempData() {
        return this.tempData;
    }

    public void setTempData(Map<String, Object> tempData) {
        this.tempData = tempData;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public InvokeDefinition getInvokeDefinition() {
        return invokeDefinition;
    }

    public void setInvokeDefinition(InvokeDefinition invokeDefinition) {
        this.invokeDefinition = invokeDefinition;
    }
}