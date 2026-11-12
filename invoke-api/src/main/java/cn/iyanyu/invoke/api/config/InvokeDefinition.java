package cn.iyanyu.invoke.api.config;

import cn.iyanyu.invoke.api.enums.CryptoType;
import cn.iyanyu.invoke.api.enums.HttpMethod;
import cn.iyanyu.invoke.api.enums.InvokeDefinitionLoad;
import cn.iyanyu.invoke.api.enums.SignType;

public class InvokeDefinition {
    private String callId = "*";
    private String internalServiceCode;
    private String targetSystemId;
    private String externalSvcApplicationId;
    private String invokerType = "aps";
    private String comProtocolId;
    private String serviceUrl;
    private HttpMethod httpMethod;
    private String externalServiceCode;
    private boolean aliasMappingFlag;
    private Object additionData;
    private String apiId;
    private SignType signType;
    private CryptoType eadType;
    private InvokeDefinitionLoad loadType;
    private boolean mockFlag;
    private int timeout;
    private int minTimeout;
    private String functionDesc;
    private String pkgConverterType;
    private String createTime;
    private String updateTime;

    public InvokeDefinition() {
        this.httpMethod = HttpMethod.POST;
        this.signType = SignType.NO;
        this.eadType = CryptoType.NO;
        this.mockFlag = false;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getInternalServiceCode() {
        return internalServiceCode;
    }

    public void setInternalServiceCode(String internalServiceCode) {
        this.internalServiceCode = internalServiceCode;
    }

    public String getTargetSystemId() {
        return targetSystemId;
    }

    public void setTargetSystemId(String targetSystemId) {
        this.targetSystemId = targetSystemId;
    }

    public String getExternalSvcApplicationId() {
        return externalSvcApplicationId;
    }

    public void setExternalSvcApplicationId(String externalSvcApplicationId) {
        this.externalSvcApplicationId = externalSvcApplicationId;
    }

    public String getInvokerType() {
        return invokerType;
    }

    public void setInvokerType(String invokerType) {
        this.invokerType = invokerType;
    }

    public String getComProtocolId() {
        return comProtocolId;
    }

    public void setComProtocolId(String comProtocolId) {
        this.comProtocolId = comProtocolId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getExternalServiceCode() {
        return externalServiceCode;
    }

    public void setExternalServiceCode(String externalServiceCode) {
        this.externalServiceCode = externalServiceCode;
    }

    public boolean isAliasMappingFlag() {
        return aliasMappingFlag;
    }

    public void setAliasMappingFlag(boolean aliasMappingFlag) {
        this.aliasMappingFlag = aliasMappingFlag;
    }

    public Object getAdditionData() {
        return additionData;
    }

    public void setAdditionData(Object additionData) {
        this.additionData = additionData;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public CryptoType getEadType() {
        return eadType;
    }

    public void setEadType(CryptoType eadType) {
        this.eadType = eadType;
    }

    public InvokeDefinitionLoad getLoadType() {
        return loadType;
    }

    public void setLoadType(InvokeDefinitionLoad loadType) {
        this.loadType = loadType;
    }

    public boolean isMockFlag() {
        return mockFlag;
    }

    public void setMockFlag(boolean mockFlag) {
        this.mockFlag = mockFlag;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMinTimeout() {
        return minTimeout;
    }

    public void setMinTimeout(int minTimeout) {
        this.minTimeout = minTimeout;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc;
    }

    public String getPkgConverterType() {
        return pkgConverterType;
    }

    public void setPkgConverterType(String pkgConverterType) {
        this.pkgConverterType = pkgConverterType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}