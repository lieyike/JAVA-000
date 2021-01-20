package io.kimmking.kmq.core;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Kmq2 {

    private String topic;

    private int capacity;

    private KmqMessage[] queue;

    private int readableIndex = 0;

    private int writeableIndex = 0;

    public Kmq2(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity];
    }


    public synchronized boolean send(KmqMessage message) {
        try {
            if (writeableIndex <= capacity) {
                queue[writeableIndex] = message;
                writeableIndex++;
            } else {
                KmqMessage[] tmp = new KmqMessage[capacity];
                int i = 0;
                for (int j = readableIndex; j < capacity; i++, j++) {
                    tmp[i] = queue[j];
                }
                readableIndex = 0;
                queue[writeableIndex] = message;
                writeableIndex = i + 1;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public synchronized KmqMessage poll() {
        if (readableIndex < writeableIndex) {
            KmqMessage message = queue[readableIndex];
            readableIndex++;
            return message;
        } else {
            return null;
        }
    }

    @SneakyThrows
    public KmqMessage poll(long timeout) {
        long startTime = System.currentTimeMillis();
        long spendTime = 0;
        while(spendTime < timeout) {
            KmqMessage message = poll();

            if (message != null) {
                return message;
            }
            Thread.sleep(10);
            spendTime = System.currentTimeMillis() - startTime;
        }
        return null;
    }

}
