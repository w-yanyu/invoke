package cn.iyanyu.invoke.api.mq.producer.retry;

import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.common.MqSendResult;
import cn.iyanyu.invoke.api.mq.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class AsyncSendRetryAction implements BiConsumer<MqSendResult, Throwable> {
    private static final Logger log = LoggerFactory.getLogger(AsyncSendRetryAction.class);
    private final Iterator<MessageProducer> iterator;
    private final MqMessage message;

    public AsyncSendRetryAction(Iterator<MessageProducer> iterator, MqMessage message) {
        this.iterator = iterator;
        this.message = message;
    }

    public void accept(MqSendResult result, Throwable throwable) {
        if (throwable != null) {
            log.warn("Send message fail. will be retry", throwable);
            MessageProducer producer = (MessageProducer)this.iterator.next();
            producer.async(this.message).whenComplete(this);
        }

    }
}