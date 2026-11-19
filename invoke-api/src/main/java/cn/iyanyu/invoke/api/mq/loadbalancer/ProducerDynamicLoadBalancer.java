package cn.iyanyu.invoke.api.mq.loadbalancer;

import cn.iyanyu.base.model.api.mq.common.MqMessage;
import cn.iyanyu.base.mq.producer.MessageProducer;
import cn.iyanyu.invoke.api.mq.producer.ProducerLifecycleManager;
import cn.iyanyu.invoke.api.mq.route.MQZoneToDcMapper;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducerDynamicLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(ProducerDynamicLoadBalancer.class);
    private final MQZoneToDcMapper mqZoneToDcMapper;
    private final ProducerLifecycleManager producerLifecycleManager;
    private final Map<String, ProducerLoadBalancer> cache = new HashMap<>();

    public ProducerDynamicLoadBalancer(MQZoneToDcMapper mqZoneToDcMapper, ProducerLifecycleManager producerLifecycleManager, List<ProducerLoadBalancer> producerLoadBalancerList) {
        this.mqZoneToDcMapper = mqZoneToDcMapper;
        this.producerLifecycleManager = producerLifecycleManager;
        producerLoadBalancerList.forEach((producerLoadBalancer) -> {
            this.cache.put(producerLoadBalancer.getLoadBalancerStrategy(), producerLoadBalancer);
        });
    }

    public MessageProducer choose(MqMessage mqMessage) {
        String targetZoneId = mqMessage.getTargetZoneId();
        boolean globalMessageFlag = mqMessage.isGlobalMessageFlag();
        String ldcLoadBalancerStrategy = mqMessage.getLdcLoadBalancerStrategy();
        if (globalMessageFlag) {
            return this.producerLifecycleManager.currentMessageProducer();
        } else {
            log.debug("targetZoneId:{}, ldcLoadBalancerStrategy:{}", targetZoneId, ldcLoadBalancerStrategy);
            List<String> targetDcIds = this.checkParamAndGetTargetDc(targetZoneId);
            ProducerLoadBalancer producerLoadBalancer = (ProducerLoadBalancer) this.cache.get(ldcLoadBalancerStrategy);
            return producerLoadBalancer.choose(targetDcIds, mqMessage);
        }
    }

    private List<String> checkParamAndGetTargetDc(String targetZoneId) {
        if (StringUtils.isBlank(targetZoneId)) {
            throw new IllegalStateException("targetZoneId can not be null.");
        } else {
            List<String> targetIds = this.mqZoneToDcMapper.getDcIdByZoneId(targetZoneId);
            if (CollectionUtils.isEmpty(targetIds)) {
                throw new IllegalStateException("TargetDcId can not be null. Please check zone-dc-mapping config.");
            } else {
                return targetIds;
            }
        }
    }
}