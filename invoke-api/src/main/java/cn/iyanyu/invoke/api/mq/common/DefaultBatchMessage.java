package cn.iyanyu.invoke.api.mq.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

class DefaultBatchMessage extends ArrayList<MqMessage> implements BatchMessage {
    private String batchNo;
    private String topic;

    DefaultBatchMessage() {
    }

    DefaultBatchMessage(String topic) {
        this.topic = topic;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return this.topic;
    }

    public boolean add(MqMessage mqMessage) {
        this.addMessage(mqMessage);
        return true;
    }

    public boolean addAll(Collection<? extends MqMessage> c) {
        for (MqMessage mqMessage : c) {
            this.addMessage(mqMessage);
        }

        return true;
    }

    public void addMessage(MqMessage message) {
        Objects.requireNonNull(this.topic, "BatchMessage must be not null.");
        message.setMessageTopic(this.topic);
        super.add(message);
    }
}