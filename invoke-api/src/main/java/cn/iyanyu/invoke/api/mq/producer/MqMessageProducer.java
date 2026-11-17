package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.common.MqTransactionSendResult;

import java.util.List;

public interface MqMessageProducer {
    MqSendResult syncSend(MqMessage message);

    default MqMessage syncSend(MqMessage message, boolean immediate) {
        if (immediate) {
            this.syncSend(message);
        }

        return message;
    }

    void asyncSend(MqMessage message);

    void batchSend(List<MqMessage> mqMessages);

    MqSendResult syncSendOrderly(MqMessage message, String orderGroup, int orderNo);

    void sendOneWay(MqMessage message);

    MqTransactionSendResult sendTransMessage(MqMessage mqMessage, Object arg);

    void sendMessageForTrans(MqMessage mqMessage);
}
