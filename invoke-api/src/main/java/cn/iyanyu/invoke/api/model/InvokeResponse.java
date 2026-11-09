package cn.iyanyu.invoke.api.model;

import cn.iyanyu.invoke.api.constants.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class InvokeResponse<RES> {
    private Class<RES> responseType;
    private RES body;
    private Map<String, String> header = new HashMap<>();
    private ResponseStatus status;
    private String errorMsg;
    private String errorCode;

    public InvokeResponse() {
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Class<RES> getResponseType() {
        return this.responseType;
    }

    public void setResponseType(Class<RES> responseType) {
        this.responseType = responseType;
    }

    public RES getBody() {
        return this.body;
    }

    public void setBody(RES body) {
        this.body = body;
    }

    public ResponseStatus getStatus() {
        return this.status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Map<String, String> getHeader() {
        return this.header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}