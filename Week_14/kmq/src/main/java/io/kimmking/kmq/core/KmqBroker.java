package io.kimmking.kmq.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class KmqBroker { // Broker+Connection

    public static final int CAPACITY = 502;

    private final Map<String, Kmq2> kmqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name){
        kmqMap.putIfAbsent(name, new Kmq2(name,CAPACITY));
    }

    public Kmq2 findKmq(String topic) {
        return this.kmqMap.get(topic);
    }

    public KmqProducer createProducer() {
        return new KmqProducer(this);
    }

    public KmqConsumer createConsumer() {
        return new KmqConsumer(this);
    }

}
