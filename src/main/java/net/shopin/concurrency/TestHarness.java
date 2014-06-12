package net.shopin.concurrency;

/**
 * TestHarness
 * <p/>
 * Using CountDownLatch for starting and stopping threads in timing tests
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.CountDownLatch;

/**
 * 说明:  CountDownLatch 闭锁的使用
 * User: kongming
 * Date: 14-6-12
 * Time: 下午2:02
 */
public class TestHarness {

    public static long timeTasks(int nThreads,final Runnable task)throws InterruptedException{
        final CountDownLatch startGate =  new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for(int i = 0;i < nThreads;i++){
            Thread t = new Thread(){
                @Override
                public void run() {
                    try {
                        startGate.await();

                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            };
            t.start();

        }
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        System.out.println("cost time : " + (end - start));
        return end - start;
    }


    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("run");
            }
        };
        try {
            timeTasks(5,runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



}
