package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;

import java.util.concurrent.CompletableFuture;

public interface TransactionConsistentMessageProducer {
    CompletableFuture<MqSendResult> send(MqMessage MQMessage);

    CompletableFuture<MqSendResult> send(String targetId, MqMessage MQMessage);
}
