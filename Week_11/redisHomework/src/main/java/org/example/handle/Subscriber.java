package org.example.handle;

import com.google.gson.Gson;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Subscriber extends JedisPubSub {

    private final static String CHANNEL_NAME = "ORDER";

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    Gson gson = new Gson();

    @Override
    public void onMessage(String channel, final String message) {
        System.out.println("Message received. Channel" + channel + " Message: " + message);
        if (CHANNEL_NAME.equals(channel)) {
            executorService.submit(() -> {
                Order order = gson.fromJson(message, Order.class);
                System.out.println("Order " + order.getId() + " was handled");
            });
        }

    }
}
