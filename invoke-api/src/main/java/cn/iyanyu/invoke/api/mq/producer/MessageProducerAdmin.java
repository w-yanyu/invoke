package cn.iyanyu.invoke.api.mq.producer;

public interface MessageProducerAdmin {
    void start(String activeId) throws Throwable;

    void stop() throws Throwable;

    static MessageProducerAdmin noop() {
        return new NoOpMessageProducerAdmin();
    }
}