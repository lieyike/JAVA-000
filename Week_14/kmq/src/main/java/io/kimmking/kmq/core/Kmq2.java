package io.kimmking.kmq.core;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.concurrent.*;

public final class Kmq2 {

    private String topic;

    private int capacity;

    private KmqMessage[] queue;

//    private int readableIndex = 0;

    private int writeableIndex = 0;

    private ConcurrentHashMap<String, Integer> readableIndexIndexMap = new ConcurrentHashMap<>(); //加了锁目前看可以直接用HashMap

    public Kmq2(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity];
    }


    public synchronized boolean send(KmqMessage message) {
        try {
            if (writeableIndex >= capacity) {
                KmqMessage[] tmp = new KmqMessage[capacity];
                int i = 0;

                int smallestReadableIndex = capacity;
                for (Integer index: readableIndexIndexMap.values()) {
                    if (smallestReadableIndex > index) {
                        smallestReadableIndex = index;
                    }
                }
                for (int j = smallestReadableIndex; j < capacity; i++, j++) {
                    tmp[i] = queue[j];
                }

                final int deviation = smallestReadableIndex;
                ConcurrentHashMap<String, Integer> tmpMap = new ConcurrentHashMap<>();
                readableIndexIndexMap.forEach((consumerId, index) -> {
                    tmpMap.put(consumerId, index - deviation);
                });
                readableIndexIndexMap = tmpMap;
                writeableIndex = i;
                queue = tmp;
            }
            queue[writeableIndex] = message;
            writeableIndex++;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public synchronized KmqMessage poll(String consumerId) {
        int readableIndex = readableIndexIndexMap.getOrDefault(consumerId, 0);
        if (readableIndex < writeableIndex) {
            KmqMessage message = queue[readableIndex];
            readableIndexIndexMap.put(consumerId, readableIndex + 1);
            return message;
        } else {
            return null;
        }
    }

    @SneakyThrows
    public KmqMessage poll(String consumerId, long timeout) {
        long startTime = System.currentTimeMillis();
        long spendTime = 0;
        while(spendTime < timeout) {
            KmqMessage message = poll(consumerId);

            if (message != null) {
                return message;
            }
            Thread.sleep(10);
            spendTime = System.currentTimeMillis() - startTime;
        }
        return null;
    }

}
