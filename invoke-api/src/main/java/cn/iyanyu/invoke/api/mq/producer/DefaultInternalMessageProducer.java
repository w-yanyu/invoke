package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.comm.factories.FactoriesLoader;
import cn.iyanyu.base.model.api.mq.common.MqMessage;
import cn.iyanyu.base.mq.common.BatchMessage;
import cn.iyanyu.base.mq.common.MqSendResult;
import cn.iyanyu.base.mq.common.MqTransactionSendResult;
import cn.iyanyu.base.mq.producer.MessageProducer;
import cn.iyanyu.base.mq.producer.MqMessageSendCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class DefaultInternalMessageProducer implements MessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInternalMessageProducer.class);
    private final MessageProducer messageProducer;

    public DefaultInternalMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    public MqSendResult send(MqMessage mqMessage) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send synchronously messages :{}", mqMessage);
        }

        try {
            return this.messageProducer.send(mqMessage);
        } catch (Throwable var4) {
            String msg = String.format("Send synchronously messages fail, globalBusinessTrackNo:%s, topic:%s", mqMessage.getGlobalBusinessTrackNo(), mqMessage.getMessageTopic());
            LOGGER.error(msg, var4);
            throw var4;
        }
    }

    public void sendBatch(BatchMessage batchMessage) {
        try {
            this.messageProducer.sendBatch(batchMessage);
        } catch (Throwable var4) {
            String msg = String.format("Send batch messages fail, batchNo:%s, topic:%s", batchMessage.getBatchNo(), batchMessage.getTopic());
            LOGGER.error(msg, var4);
            throw var4;
        }
    }

    public void sendBatchAsync(BatchMessage batchMessage) {
        try {
            this.messageProducer.sendBatchAsync(batchMessage);
        } catch (Throwable var4) {
            String msg = String.format("Send batch messages fail, batchNo:%s, topic:%s", batchMessage.getBatchNo(), batchMessage.getTopic());
            LOGGER.error(msg, var4);
            throw var4;
        }
    }

    public CompletableFuture<MqSendResult> async(MqMessage mqMessage) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send message async:{}", mqMessage);
        }

        return this.messageProducer.async(mqMessage).whenComplete((mqSendResult, throwable) -> {
            MqMessageSendCallback mqMessageSendCallback = (MqMessageSendCallback) FactoriesLoader.getFactoryById(MqMessageSendCallback.class, mqMessage.getMessageTopic());
            if (throwable != null) {
                String msg = String.format("Failed to send message asynchronously, globalBusinessTrackNo:%s.", mqMessage.getGlobalBusinessTrackNo());
                LOGGER.error(msg, throwable);
                if (mqMessageSendCallback != null) {
                    mqMessageSendCallback.exceptionCallback(mqMessage, throwable);
                }
            } else {
                LOGGER.info("Success in sending message asynchronously, globalBusinessTrackNo：{}, send result：{}", mqMessage.getGlobalBusinessTrackNo(), mqSendResult);
                if (mqMessageSendCallback != null) {
                    mqMessageSendCallback.successCallback(mqMessage, mqSendResult);
                }
            }

        });
    }

    public void oneway(MqMessage mqMessage) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Send oneway message：{}", mqMessage);
            }

            this.messageProducer.oneway(mqMessage);
        } catch (Throwable var3) {
            LOGGER.error("Send oneway message fail. ", var3);
            throw var3;
        }
    }

    public MqTransactionSendResult sendTransactionMessage(MqMessage mqMessage, Object arg) {
        LOGGER.debug("Send transaction messages：{}", mqMessage);
        MqTransactionSendResult mqTransactionSendResult = this.messageProducer.sendTransactionMessage(mqMessage, arg);
        LOGGER.info("Send transaction messages, globalBusinessTrackNo:{}, sendResult:{}", mqMessage.getGlobalBusinessTrackNo(), mqTransactionSendResult);
        return mqTransactionSendResult;
    }
}