package net.shopin.concurrency;

import junit.framework.TestCase;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PutTakeTest
 * <p/>
 * Producer-consumer test program for BoundedBuffer
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-25
 * Time: 下午3:36
 */
public class PutTakeTest extends TestCase{

    protected static  final ExecutorService pool = Executors.newCachedThreadPool();
    protected CyclicBarrier barrier;
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrials,nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0);
    protected final AtomicInteger takeSum = new AtomicInteger(0);


    public PutTakeTest(int capacity,int nPairs,int nTrials) {
        this.bb = new SemaphoreBoundedBuffer<Integer>(capacity);
        this.nTrials = nTrials;
        this.nPairs = nPairs;
        //初始化参数为工作线程数+1
        this.barrier = new CyclicBarrier(nPairs * 2 + 1);
    }


    public static void main(String[] args) {
        new PutTakeTest(10,5,100).test();
        pool.shutdown();

    }

    void test(){
        for (int i = 0;i<nPairs;i++){
            pool.execute(new Producer());
            pool.execute(new Consumer());
        }
        try {
            barrier.await();//wait for all threads to be ready
            barrier.await();//wait for all threads to finish
            assertEquals(putSum.get(),takeSum.get());
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }



    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable{

        @Override
        public void run() {
            int seed = (this.hashCode()^(int)System.nanoTime());
            int sum = 0;
            try {
                barrier.await();
                for (int i = nTrials;i>0;i--){
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    class Consumer implements Runnable{


        @Override
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials;i>0;i--){
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BrokenBarrierException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }





}
