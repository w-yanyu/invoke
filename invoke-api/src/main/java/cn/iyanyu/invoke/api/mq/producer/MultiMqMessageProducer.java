package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.common.MqTransactionSendResult;

import java.util.List;

public interface MultiMqMessageProducer {
    MqSendResult syncSend(Object targetId, MqMessage message);

    void asyncSend(String targetId, MqMessage message);

    void batchSend(String targetId, List<MqMessage> mqMessages);

    MqSendResult syncSendOrderly(String targetId, MqMessage message, String orderGroup, int orderNo);

    void sendOneWay(String targetId, MqMessage message);

    MqTransactionSendResult sendTransMessage(String targetId, MqMessage mqMessage, Object arg);

    void sendMessageForTrans(String targetId, MqMessage mqMessage);
}
