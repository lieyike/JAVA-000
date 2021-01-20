package io.kimmking.kmq.core;

import java.util.UUID;

public class KmqConsumer<T> {

    private String consumerId;

    private final KmqBroker broker;

    private Kmq2 kmq;

    public KmqConsumer(KmqBroker broker) {
        this.broker = broker;
        this.consumerId = UUID.randomUUID().toString();
    }

    public void subscribe(String topic) {
        this.kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public KmqMessage<T> poll(long timeout) {
        return kmq.poll(consumerId, timeout);
    }

}
