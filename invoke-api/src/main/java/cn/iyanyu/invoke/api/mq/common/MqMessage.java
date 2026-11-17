package cn.iyanyu.invoke.api.mq.common;

import cn.iyanyu.invoke.api.route.RouteQueryEntity;

import java.util.HashMap;
import java.util.Map;

public class MqMessage {
    private String globalBusinessTrackNo;
    private boolean globalMessageFlag = true;
    private boolean multEnvFlag = false;
    private RouteQueryEntity routeQueryEntity;
    private String messageTopic;
    private String tags = "*";
    private String orderGroup;
    private int orderNo;
    private Object messageBody;
    private int delayTimeLevel;
    private String targetZoneId;
    private String targetDcId;
    private String targetEnvId;
    private Map<String, String> properties;
    private String batchNo;
    private String ldcLoadBalancerStrategy = "Random";
    private long deliveryTimestamp;
    private String partitionKey;
    private Integer partition;

    public MqMessage() {
    }

    public void putUserProperty(String name, String value) {
        if (null == this.properties) {
            this.properties = new HashMap<>();
        }

        this.properties.put(name, value);
    }

    public String getGlobalBusinessTrackNo() {
        return this.globalBusinessTrackNo;
    }

    public void setGlobalBusinessTrackNo(String globalBusinessTrackNo) {
        this.globalBusinessTrackNo = globalBusinessTrackNo;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOrderGroup() {
        return this.orderGroup;
    }

    public void setOrderGroup(String orderGroup) {
        this.orderGroup = orderGroup;
    }

    public int getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getDelayTimeLevel() {
        return this.delayTimeLevel;
    }

    public void setDelayTimeLevel(int delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public boolean isGlobalMessageFlag() {
        return this.globalMessageFlag;
    }

    public void setGlobalMessageFlag(boolean globalMessageFlag) {
        this.globalMessageFlag = globalMessageFlag;
    }

    public String getMessageTopic() {
        return this.messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public Object getMessageBody() {
        return this.messageBody;
    }

    public void setMessageBody(Object messageBody) {
        this.messageBody = messageBody;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLdcLoadBalancerStrategy() {
        return this.ldcLoadBalancerStrategy;
    }

    public void setLdcLoadBalancerStrategy(String ldcLoadBalancerStrategy) {
        this.ldcLoadBalancerStrategy = ldcLoadBalancerStrategy;
    }

    public String getTargetZoneId() {
        return this.targetZoneId;
    }

    public void setTargetZoneId(String targetZoneId) {
        this.targetZoneId = targetZoneId;
    }

    public String getTargetDcId() {
        return this.targetDcId;
    }

    public void setTargetDcId(String targetDcId) {
        this.targetDcId = targetDcId;
    }

    public String getTargetEnvId() {
        return this.targetEnvId;
    }

    public void setTargetEnvId(String targetEnvId) {
        this.targetEnvId = targetEnvId;
    }

    public RouteQueryEntity getRouteQueryEntity() {
        return this.routeQueryEntity;
    }

    public void setRouteQueryEntity(RouteQueryEntity routeQueryEntity) {
        this.routeQueryEntity = routeQueryEntity;
    }

    public long getDeliveryTimestamp() {
        return this.deliveryTimestamp;
    }

    public void setDeliveryTimestamp(long deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    public boolean isMultEnvFlag() {
        return this.multEnvFlag;
    }

    public void setMultEnvFlag(boolean multEnvFlag) {
        this.multEnvFlag = multEnvFlag;
    }

    public String getPartitionKey() {
        return this.partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public Integer getPartition() {
        return this.partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public String toString() {
        return "MqMessage{globalBusinessTrackNo='" + this.globalBusinessTrackNo + "', globalMessageFlag=" + this.globalMessageFlag + ", multEnvFlag=" + this.multEnvFlag + ", routeQueryEntity=" + this.routeQueryEntity + ", messageTopic='" + this.messageTopic + "', tags='" + this.tags + "', orderGroup='" + this.orderGroup + "', orderNo=" + this.orderNo + ", messageBody=" + this.messageBody + ", delayTimeLevel=" + this.delayTimeLevel + ", targetZoneId='" + this.targetZoneId + "', targetDcId='" + this.targetDcId + "', targetEnvId='" + this.targetEnvId + "', properties=" + this.properties + ", batchNo='" + this.batchNo + "', ldcLoadBalancerStrategy='" + this.ldcLoadBalancerStrategy + "', deliveryTimestamp=" + this.deliveryTimestamp + ", partitionKey='" + this.partitionKey + "', partition=" + this.partition + "}";
    }
}