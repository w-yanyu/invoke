package cn.iyanyu.invoke.api.mq.producer;

import cn.iyanyu.base.mq.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleProducerLifecycleManager implements ProducerLifecycleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProducerLifecycleManager.class);
    private final LinkedHashMap<String, MessageProducerFactory> producers = new LinkedHashMap<>();
    private final ConcurrentMap<String, ProducerHolder> holders = new ConcurrentHashMap<>();
    private volatile String currentActive;
    private volatile List<String> allActive = new ArrayList<>();

    public SimpleProducerLifecycleManager() {
    }

    public void putProducer(String id, MessageProducerFactory factory) {
        this.producers.put(id, factory);
    }

    public void removeProducer(String id) {
        this.producers.remove(id);
    }

    public void activeProducer(String id) throws Throwable {
        MessageProducer messageProducer = this.getMessageProducer(id);
        if (messageProducer != null) {
            LOGGER.info("active producer [{}] success.", id);
        }

        this.currentActive = id;
    }

    public String currentActive() {
        return this.currentActive;
    }

    public List<String> allActive() {
        return this.allActive;
    }

    public void setAllActive(List<String> list) {
        this.allActive = list;
    }

    public MessageProducer currentMessageProducer() {
        return this.getMessageProducer(this.currentActive);
    }

    public MessageProducer getMessageProducer(String targetClusterId) {
        ProducerHolder holder = (ProducerHolder)this.holders.computeIfAbsent(targetClusterId, ProducerHolder::new);
        MessageProducer messageProducer = holder.getMessageProducer();
        if (messageProducer != null) {
            return messageProducer;
        } else {
            synchronized(holder) {
                messageProducer = holder.getMessageProducer();
                if (messageProducer != null) {
                    return messageProducer;
                }

                MessageProducerFactory messageProducerFactory = (MessageProducerFactory)this.producers.get(targetClusterId);
                Objects.requireNonNull(messageProducerFactory, "'" + targetClusterId + "' not found producer");
                MessageProducerFactory.Producers producer = messageProducerFactory.createMessageProducer();
                holder.setMessageProducer(producer.messageProducer());
                holder.setMessageProducerAdmin(producer.admin());

                try {
                    producer.admin().start(targetClusterId);
                } catch (Throwable var9) {
                    LOGGER.error("Start MQ Producer fail.", var9);
                    throw new IllegalStateException(var9);
                }
            }

            return holder.getMessageProducer();
        }
    }

    public List<String> getAllProducers() {
        return new ArrayList<>(this.producers.keySet());
    }

    private static class ProducerHolder {
        private volatile MessageProducer messageProducer;
        private volatile MessageProducerAdmin messageProducerAdmin;
        private final String targetClusterId;

        private ProducerHolder(String targetClusterId) {
            this.targetClusterId = targetClusterId;
        }

        public MessageProducer getMessageProducer() {
            return this.messageProducer;
        }

        public void setMessageProducer(MessageProducer messageProducer) {
            this.messageProducer = messageProducer;
        }

        public void setMessageProducerAdmin(MessageProducerAdmin messageProducerAdmin) {
            this.messageProducerAdmin = messageProducerAdmin;
        }
    }
}