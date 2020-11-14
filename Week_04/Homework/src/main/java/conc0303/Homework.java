package conc0303;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework {

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        //        int result = sum(); //这是得到的返回值

        //Way 1 thread join
//        AtomicInteger atomicInteger = new AtomicInteger();
//        Thread thread = new Thread(new Task1(atomicInteger));
//        thread.start();
//        thread.join();
//        int result = atomicInteger.get();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //Way 2 countDownLatch
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        AtomicInteger atomicInteger = new AtomicInteger();
//        Task2 task = new Task2(countDownLatch, atomicInteger);
//
//        Thread thread = new Thread(task);
//        thread.start();
//        countDownLatch.await();
//        int result = atomicInteger.get();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //Way 3 Future
//        Callable<Integer> callable = new Callable<Integer>() {
//            public Integer call() throws Exception {
//                return sum();
//            }
//        };
//
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Future<Integer> future = executor.submit(callable);
//        int result = future.get();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //Way 4 FutureTask
//        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
//            public Integer call() throws Exception {
//                return sum();
//            }
//        });
//
//        Thread thread = new Thread(task);
//        thread.start();
//        int result = task.get();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //Way 5 CompletableFuture
//        int result = CompletableFuture.supplyAsync(Homework::sum).join();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //Way 6 cyclicBarrier 不是在主线程拿到result而是在回调中拿到
//        AtomicInteger atomicInteger = new AtomicInteger();
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
//            @Override
//            public void run() {
//                int result = atomicInteger.get();
//                System.out.println("异步计算结果为："+result);
//                System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
//            }
//        });
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                atomicInteger.set(sum());
//                try {
//                    cyclicBarrier.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();

        //Way 7 LockSupport
//        Thread main = Thread.currentThread();
//        AtomicInteger atomicInteger = new AtomicInteger();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                atomicInteger.set(sum());
//                LockSupport.unpark(main);
//            }
//        });
//        thread.start();
//        LockSupport.park();
//        int result = atomicInteger.get();
//        System.out.println("异步计算结果为："+result);
//        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");


        //Way 8 wait and notifyAll
//        Homework homework = new Homework();
//        AtomicInteger atomicInteger = new AtomicInteger();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                homework.sumAndNotifyAll(atomicInteger);
//            }
//        });
//        thread.start();
//        homework.printResult(atomicInteger, start);

        //Way 9
//        Thread main = Thread.currentThread();
//        AtomicInteger atomicInteger = new AtomicInteger();
//        AtomicBoolean flag = new AtomicBoolean(true);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                atomicInteger.set(sum());
//                flag.set(false);
//            }
//        });
//        thread.start();
//
//        while (flag.get()) {
//            Thread.sleep(50);
//        }
//        int result = atomicInteger.get();
//        System.out.println("异步计算结果为：" + result);
//        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        //Way 10 semaphore
        Semaphore semaphore = new Semaphore(1, true);

        AtomicInteger atomicInteger = new AtomicInteger();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                atomicInteger.set(sum());
                semaphore.release();
            }
        });
        semaphore.acquire(1);
        thread.start();

        semaphore.acquire(1);
        int result = atomicInteger.get();
        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }

    private synchronized void printResult(AtomicInteger atomicInteger, long start) throws InterruptedException {
        wait();
        int result = atomicInteger.get();
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    private synchronized void sumAndNotifyAll(AtomicInteger atomicInteger) {
        atomicInteger.set(sum());
        notifyAll();
    }

    private static int sum1(int a) {
        if (a <= 0) {
            return 0;
        }

        if (a == 1) {
            return 1;
        }

        int f1 = 1;
        int f2 = 1;
        int result = 0;
        int i = 2;
        while (i++ <= a) {
            result = f1 + f2;
            f1 = f2;
            f2 = result;
        }
        return result;
    }


    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }

    private static class Task1 implements Runnable {
        AtomicInteger result;

        public Task1(AtomicInteger result) {
            this.result = result;
        }

        public void run() {
            result.set(sum());
        }
    }


    private static class Task2 implements Runnable {
        CountDownLatch countDownLatch;
        AtomicInteger result;

        public Task2(CountDownLatch countDownLatch, AtomicInteger result) {
            this.countDownLatch = countDownLatch;
            this.result = result;
        }

        public void run() {
            result.set(sum());
            countDownLatch.countDown();
        }
    }


}
