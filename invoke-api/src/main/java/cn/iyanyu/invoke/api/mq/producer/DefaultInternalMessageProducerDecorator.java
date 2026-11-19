package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.mq.producer.MessageProducer;
import cn.iyanyu.base.mq.producer.MessageProducerDecorator;

public class DefaultInternalMessageProducerDecorator implements MessageProducerDecorator {
    public DefaultInternalMessageProducerDecorator() {
    }

    public MessageProducer decorate(MessageProducer messageProducer) {
        return new DefaultInternalMessageProducer(messageProducer);
    }
}