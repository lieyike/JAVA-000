package org.example.counter;

import org.example.lock.RedisLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CounterDemo {

    private static String INVENTORY_KEY = "inventory";
    private static Integer NUM = 1000;
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(NUM, NUM, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        final CountDownLatch countDownLatch = new CountDownLatch(NUM);

        long start = System.currentTimeMillis();
        final JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        final Jedis jedis = jedisPool.getResource();
        jedis.set(INVENTORY_KEY, "1000");
        jedis.close();
        for (int i = 0; i < NUM; i++) {
            threadPoolExecutor.execute(new Runnable() {
                public void run() {
                    Jedis jedis = jedisPool.getResource();
                    jedis.decr(INVENTORY_KEY);
                    System.out.println("线程执行： " + Thread.currentThread().getName());
                    countDownLatch.countDown();
                    jedis.close();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }


        threadPoolExecutor.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("执行线程数:" + NUM + "总耗时： " + (end - start) + "库存数为： " + jedis.get(INVENTORY_KEY));
        jedis.close();
    }
}
