package org.example.handle;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.UUID;

public class HandleOrderDemo {

    private final static JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
    private final static String CHANNEL_NAME = "ORDER";

    public static void main(String[] args) {
        final Subscriber subscriber = new Subscriber();
        final Jedis subscriberJedis = jedisPool.getResource();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
            }
        });
        thread.start();

        Jedis publisherJedis = jedisPool.getResource();

        Gson gson = new Gson();
        Order order = new Order(UUID.randomUUID().toString(), "User1", new BigDecimal(100));
        String message = gson.toJson(order, Order.class);
        publisherJedis.publish(CHANNEL_NAME, message);


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
