package org.example.lock;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LockDemo {

    private static Integer inventory = 1000;
    private static Integer NUM = 1000;
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(NUM, NUM, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        final CountDownLatch countDownLatch = new CountDownLatch(NUM);

        long start = System.currentTimeMillis();
        final RedisLock redisLock = new RedisLock();

        for (int i = 0; i < NUM; i++) {
            threadPoolExecutor.execute(new Runnable() {
                public void run() {
                    String id = UUID.randomUUID().toString();
                    redisLock.lock(id);
                    inventory--;
                    redisLock.unlock(id);
                    System.out.println("线程执行： " + Thread.currentThread().getName());
                    countDownLatch.countDown();
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
        System.out.println("执行线程数:" + NUM + "总耗时： " + (end - start) + "库存数为： " + inventory);
    }

}
