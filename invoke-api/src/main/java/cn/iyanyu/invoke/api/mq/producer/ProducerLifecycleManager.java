package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.mq.producer.MessageProducer;

import java.util.List;

public interface ProducerLifecycleManager {
    void activeProducer(String id) throws Throwable;

    String currentActive();

    List<String> allActive();

    void setAllActive(List<String> list);

    MessageProducer currentMessageProducer();

    MessageProducer getMessageProducer(String targetClusterId);

    List<String> getAllProducers();
}