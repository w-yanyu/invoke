package cn.iyanyu.invoke.api.mq.common;

import java.util.Collection;
import java.util.Iterator;

public interface BatchMessage extends Collection<MqMessage> {
    void setBatchNo(String batchNo);

    String getBatchNo();

    String getTopic();

    void setTopic(String topic);

    void addMessage(MqMessage message);

    default void addMessages(Iterable<MqMessage> mqMessages) {
        for (MqMessage mqMessage : mqMessages) {
            this.addMessage(mqMessage);
        }

    }

    static BatchMessage newBatchMessage() {
        return new DefaultBatchMessage();
    }

    static BatchMessage newBatchMessage(String topic) {
        return new DefaultBatchMessage(topic);
    }
}
