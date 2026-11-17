package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.invoke.api.mq.common.BatchMessage;
import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.common.MqTransactionSendResult;

import java.util.concurrent.CompletableFuture;

public interface MessageProducer {
    MqSendResult send(MqMessage message);

    default CompletableFuture<MqSendResult> async(MqMessage mqMessage) {
        throw new UnsupportedOperationException("The producer unsupported async");
    }

    default void sendBatch(BatchMessage message) {
        throw new UnsupportedOperationException("The producer unsupported sendBatch");
    }

    default void sendBatchAsync(BatchMessage message) {
        throw new UnsupportedOperationException("The producer unsupported sendBatchAsync");
    }

    default void oneway(MqMessage mqMessage) {
        throw new UnsupportedOperationException("The producer unsupported oneway");
    }

    default MqTransactionSendResult sendTransactionMessage(MqMessage mqMessage, Object arg) {
        throw new UnsupportedOperationException("The producer unsupported sendTransactionMessage");
    }
}