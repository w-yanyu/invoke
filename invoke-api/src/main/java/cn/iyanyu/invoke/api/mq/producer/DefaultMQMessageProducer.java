package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.invoke.api.mq.common.BatchMessage;
import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.common.MqTransactionSendResult;
import cn.iyanyu.invoke.api.mq.loadbalancer.ProducerDynamicLoadBalancer;
import cn.iyanyu.invoke.api.mq.producer.retry.AsyncSendRetryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultMQMessageProducer implements MqMessageProducer, MultiMqMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMQMessageProducer.class);
    private final ProducerLifecycleManager producerLifecycleManager;
    private final TransactionConsistentMessageProducer transactionConsistentMessageProducer;
    private final ProducerDynamicLoadBalancer producerDynamicLoadBalancer;
    private final boolean retryNextServerOnError;

    public DefaultMQMessageProducer(ProducerLifecycleManager producerLifecycleManager, TransactionConsistentMessageProducer transactionConsistentMessageProducer, ProducerDynamicLoadBalancer producerDynamicLoadBalancer, boolean retryNextServerOnError) {
        this.producerLifecycleManager = producerLifecycleManager;
        this.transactionConsistentMessageProducer = transactionConsistentMessageProducer;
        this.producerDynamicLoadBalancer = producerDynamicLoadBalancer;
        this.retryNextServerOnError = retryNextServerOnError;
    }

    public MqSendResult syncSend(MqMessage mqMessage) {
        MessageProducer producer = this.producerDynamicLoadBalancer.choose(mqMessage);
        if (!this.retryNextServerOnError) {
            return producer.send(mqMessage);
        } else {
            try {
                return producer.send(mqMessage);
            } catch (Throwable var4) {
                LOGGER.warn("Send message fail. will be retry", var4);
                return (MqSendResult) this.retry((targetId) -> this.syncSend(targetId, mqMessage));
            }
        }
    }

    public void asyncSend(MqMessage mqMessage) {
        MessageProducer producer = this.producerDynamicLoadBalancer.choose(mqMessage);
        if (!this.retryNextServerOnError) {
            producer.async(mqMessage);
        } else {
            List<String> allProducers = this.producerLifecycleManager.getAllProducers();
            Iterator<String> idIterator = allProducers.iterator();
            String targetId = idIterator.next();
            CompletableFuture<MqSendResult> async = this.getProducer(targetId).async(mqMessage);
            Iterator<MessageProducer> it = new MessageProducerIterator(idIterator);
            async.whenComplete(new AsyncSendRetryAction(it, mqMessage));
        }
    }

    public void batchSend(List<MqMessage> list) {
        BatchMessage batchMessage = BatchMessage.newBatchMessage(((MqMessage) list.get(0)).getMessageTopic());
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        batchMessage.setBatchNo(batchNo);
        MessageProducer producer = this.producerDynamicLoadBalancer.choose((MqMessage) list.get(0));
        if (!this.retryNextServerOnError) {
            producer.sendBatch(batchMessage);
        } else {
            try {
                producer.sendBatch(batchMessage);
            } catch (Throwable var6) {
                LOGGER.warn("Send message fail. will be retry", var6);
                this.retryUnBack((targetId) -> {
                    producer.sendBatch(batchMessage);
                });
            }
        }
    }

    public MqSendResult syncSendOrderly(MqMessage mqMessage, String orderGroup, int orderNo) {
        mqMessage.setOrderGroup(orderGroup);
        mqMessage.setOrderNo(orderNo);
        return this.producerLifecycleManager.currentMessageProducer().send(mqMessage);
    }

    public void sendOneWay(MqMessage mqMessage) {
        MessageProducer producer = this.producerDynamicLoadBalancer.choose(mqMessage);
        if (!this.retryNextServerOnError) {
            producer.oneway(mqMessage);
        } else {
            try {
                producer.oneway(mqMessage);
            } catch (Throwable var4) {
                LOGGER.warn("Send message fail. will be retry", var4);
                this.retryUnBack((targetId) -> {
                    producer.oneway(mqMessage);
                });
            }
        }
    }

    public MqTransactionSendResult sendTransMessage(MqMessage mqMessage, Object o) {
        MessageProducer producer = this.producerDynamicLoadBalancer.choose(mqMessage);
        if (!this.retryNextServerOnError) {
            return producer.sendTransactionMessage(mqMessage, o);
        } else {
            try {
                return producer.sendTransactionMessage(mqMessage, o);
            } catch (Throwable var5) {
                LOGGER.warn("Send message fail. will be retry", var5);
                return this.retry((targetId) -> producer.sendTransactionMessage(mqMessage, o));
            }
        }
    }

    public void sendMessageForTrans(MqMessage mqMessage) {
        Assert.notNull(this.transactionConsistentMessageProducer, "transactionConsistentMessageProducer must be not null. Please use 'aps.dal' datasource");
        this.transactionConsistentMessageProducer.send(mqMessage);
    }

    public MqSendResult syncSend(Object targetId, MqMessage message) {
        return this.getProducer((String) targetId).send(message);
    }

    public void asyncSend(String targetId, MqMessage message) {
        this.getProducer(targetId).async(message);
    }

    public void batchSend(String targetId, List<MqMessage> list) {
        BatchMessage batchMessage = BatchMessage.newBatchMessage(((MqMessage) list.get(0)).getMessageTopic());
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        batchMessage.setBatchNo(batchNo);
        this.getProducer(targetId).sendBatch(batchMessage);
    }

    public MqSendResult syncSendOrderly(String targetId, MqMessage message, String orderGroup, int orderNo) {
        message.setOrderGroup(orderGroup);
        message.setOrderNo(orderNo);
        return this.getProducer(targetId).send(message);
    }

    public void sendOneWay(String targetId, MqMessage message) {
        this.getProducer(targetId).oneway(message);
    }

    public MqTransactionSendResult sendTransMessage(String targetId, MqMessage mqMessage, Object arg) {
        return this.getProducer(targetId).sendTransactionMessage(mqMessage, arg);
    }

    private MessageProducer getProducer(String targetId) {
        return this.producerLifecycleManager.getMessageProducer(targetId);
    }

    public void sendMessageForTrans(String targetId, MqMessage mqMessage) {
        this.transactionConsistentMessageProducer.send(targetId, mqMessage);
    }

    private <R> R retry(Function<String, R> callable) {
        List<String> allProducers = this.producerLifecycleManager.allActive();
        if (CollectionUtils.isEmpty(allProducers)) {
            throw new IllegalStateException("Could not found mq server properties.");
        } else {
            LinkedHashSet<String> targetIds = new LinkedHashSet<>(allProducers);
            Throwable throwable = null;

            for (String targetId : targetIds) {
                try {
                    return callable.apply(targetId);
                } catch (Throwable var8) {
                    if (throwable != null) {
                        throwable.addSuppressed(var8);
                    } else {
                        throwable = var8;
                    }
                }
            }

            throw new IllegalStateException(throwable);
        }
    }

    private void retryUnBack(Consumer<String> action) {
        List<String> allProducers = this.producerLifecycleManager.getAllProducers();
        if (CollectionUtils.isEmpty(allProducers)) {
            throw new IllegalStateException("Could not found mq server properties.");
        } else {
            LinkedHashSet<String> targetIds = new LinkedHashSet<>(allProducers);
            Throwable throwable = null;

            for (String targetId : targetIds) {
                try {
                    action.accept(targetId);
                    break;
                } catch (Throwable var8) {
                    if (throwable != null) {
                        throwable.addSuppressed(var8);
                    } else {
                        throwable = var8;
                    }
                }
            }

            if (throwable != null) {
                throw new IllegalStateException("All attempts failed.", throwable);
            }
        }
    }

    private class MessageProducerIterator implements Iterator<MessageProducer> {
        private final Iterator<String> idIterator;

        private MessageProducerIterator(Iterator<String> idIterator) {
            this.idIterator = idIterator;
        }

        public boolean hasNext() {
            return this.idIterator.hasNext();
        }

        public MessageProducer next() {
            String next = this.idIterator.next();
            return DefaultMQMessageProducer.this.getProducer(next);
        }
    }
}
