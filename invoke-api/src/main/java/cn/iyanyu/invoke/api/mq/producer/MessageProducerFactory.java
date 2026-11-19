package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.mq.producer.MessageProducer;

@FunctionalInterface
public interface MessageProducerFactory {
    Producers createMessageProducer();

    record Producers(MessageProducer messageProducer, MessageProducerAdmin admin) {
    }
}
