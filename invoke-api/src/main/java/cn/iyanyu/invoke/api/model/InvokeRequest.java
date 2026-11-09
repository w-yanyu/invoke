package cn.iyanyu.invoke.api.model;

import cn.iyanyu.invoke.api.route.RouteResult;

import java.util.HashMap;
import java.util.Map;

public class InvokeRequest {
    public Map<String, Object> requestParams;
    public Map<String, String> requestHeader = new HashMap<>();
    private String callId = "*";
    public String internalServiceCode;
    public boolean isAsync;
    public RouteResult routeResult = new RouteResult(true);

    public InvokeRequest() {
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Map<String, Object> getRequestParams() {
        return this.requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public String getInternalServiceCode() {
        return this.internalServiceCode;
    }

    public void setInternalServiceCode(String internalServiceCode) {
        this.internalServiceCode = internalServiceCode;
    }

    public boolean isAsync() {
        return this.isAsync;
    }

    public void setAsync(boolean async) {
        this.isAsync = async;
    }

    public Map<String, String> getRequestHeader() {
        return this.requestHeader;
    }

    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public RouteResult getRouteResult() {
        return this.routeResult;
    }

    public void setRouteResult(RouteResult routeResult) {
        this.routeResult = routeResult;
    }
}