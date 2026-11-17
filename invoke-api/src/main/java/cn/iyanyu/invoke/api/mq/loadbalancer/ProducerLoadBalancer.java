package cn.iyanyu.invoke.api.mq.loadbalancer;

import cn.iyanyu.invoke.api.mq.common.MqMessage;
import cn.iyanyu.invoke.api.mq.producer.MessageProducer;

import java.util.List;

public interface ProducerLoadBalancer {
    String getLoadBalancerStrategy();

    MessageProducer choose(List<String> ldcIds, MqMessage mqMessage);
}
