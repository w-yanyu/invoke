package cn.iyanyu.autoconfigure.mq;

import cn.iyanyu.base.mq.config.NameServerInfo;
import cn.iyanyu.base.mq.consumer.MqInitListener;
import cn.iyanyu.base.mq.producer.MessageProducerDecorator;
import cn.iyanyu.invoke.api.mq.producer.DefaultInternalMessageProducerDecorator;
import cn.iyanyu.invoke.api.mq.producer.MessageProducerFactory;
import cn.iyanyu.invoke.api.mq.producer.ProducerLifecycleManager;
import cn.iyanyu.invoke.api.mq.producer.SimpleProducerLifecycleManager;
import cn.iyanyu.rocketmq.core.autoconfigure.ConditionalOnRocketMQProducer;
import cn.iyanyu.rocketmq.core.autoconfigure.RocketMQProducerProperties;
import io.micrometer.common.util.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Iterator;
import java.util.Map;

@AutoConfigureOrder(Integer.MAX_VALUE)
@ConditionalOnRocketMQProducer
@ConditionalOnProducerEnabled
@EnableConfigurationProperties({RocketMQProducerProperties.class})
@ConditionalOnClass({MqInitListener.class, RocketMQProducerProperties.class})
public class RocketMQProducerAutoConfiguration {
    public RocketMQProducerAutoConfiguration() {
    }

    @Bean
    public MessageProducerDecorator messageProducerDecorator(RocketMQProducerProperties mqProducerProperties) {
        return new DefaultInternalMessageProducerDecorator();
    }

    @Bean
    public ProducerLifecycleManager defaultMQProducerLifecycleManager(RocketMQProducerProperties mqProducerProperties, MessageProducerDecorator decorator, MqProducerProperties producerProperties) {
        Map<String, NameServerInfo> nameServerInfoMap = producerProperties.getServers();
        SimpleProducerLifecycleManager manager = new SimpleProducerLifecycleManager();
        Iterator var6 = nameServerInfoMap.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, NameServerInfo> entry = (Map.Entry)var6.next();
            String id = (String)entry.getKey();
            String address = ((NameServerInfo)entry.getValue()).getAddress();
            boolean enabled = ((NameServerInfo)entry.getValue()).isEnabled();
            if (enabled) {
                MessageProducerFactory factory = () -> {
                    AclClientRPCHook rpcHook = null;
                    if (StringUtils.isNotBlank(mqProducerProperties.getRocketmq().getAccessKey()) && StringUtils.isNotBlank(mqProducerProperties.getRocketmq().getSecretKey())) {
                        rpcHook = new AclClientRPCHook(new SessionCredentials(mqProducerProperties.getRocketmq().getAccessKey(), mqProducerProperties.getRocketmq().getSecretKey()));
                    }

                    TransactionMQProducer producer = RocketMqUtil.createDefaultMQProducer(MqUtil.consumerGroup(), rpcHook, mqProducerProperties.getRocketmq().isEnableMsgTrace(), mqProducerProperties.getRocketmq().getCustomizedTraceTopic());
                    producer.setNamesrvAddr(address.trim());
                    producer.setSendMsgTimeout(mqProducerProperties.getRocketmq().getSendMsgTimeout());
                    producer.setCompressMsgBodyOverHowmuch(mqProducerProperties.getRocketmq().getCompressMsgBodyOverHowmuch());
                    producer.setRetryTimesWhenSendFailed(mqProducerProperties.getRocketmq().getRetryTimesWhenSendFailed());
                    producer.setRetryTimesWhenSendAsyncFailed(mqProducerProperties.getRocketmq().getRetryTimesWhenSendAsyncFailed());
                    producer.setMaxMessageSize(mqProducerProperties.getRocketmq().getMaxMessageSize());
                    producer.setDefaultTopicQueueNums(mqProducerProperties.getRocketmq().getTopicQueueNums());
                    List<MqTransactionMessageListener> listeners = FactoriesLoader.getAllFactories(MqTransactionMessageListener.class);
                    if (!CollectionUtils.isEmpty(listeners)) {
                        MqUtil.checkTransactionListener(listeners);
                        producer.setTransactionListener(new ProducerTransactionListener());
                    }

                    RocketMessageProducer rocketMessageProducer = new RocketMessageProducer();
                    rocketMessageProducer.setRocketMQProducer(producer);
                    rocketMessageProducer.setProducerProperties(mqProducerProperties, producerProperties);
                    return new MessageProducerFactory.Producers(decorator.decorate(rocketMessageProducer), new RocketMessageProducerAdmin(rocketMessageProducer));
                };
                manager.putProducer(id, factory);
            }
        }

        return manager;
    }
}