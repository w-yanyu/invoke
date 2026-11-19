package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.model.api.mq.common.MqMessage;
import cn.iyanyu.base.mq.common.MqSendResult;

import java.util.concurrent.CompletableFuture;

public interface TransactionConsistentMessageProducer {
    CompletableFuture<MqSendResult> send(MqMessage MQMessage);

    CompletableFuture<MqSendResult> send(String targetId, MqMessage MQMessage);
}
