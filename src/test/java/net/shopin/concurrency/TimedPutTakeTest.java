package net.shopin.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-25
 * Time: 下午5:56
 */
public class TimedPutTakeTest extends PutTakeTest{

    private BarrierTimer timer = new BarrierTimer();

    public TimedPutTakeTest(int capacity, int nPairs, int nTrials) {
        super(capacity, nPairs, nTrials);
        barrier = new CyclicBarrier(nPairs*2+1,timer);
    }

    public void test(){

        try {
            timer.clear();
            for (int i = 0;i < nPairs;i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();
            barrier.await();
            long nsPerTime = timer.getTime()/(nPairs*(long)nTrials);
            System.out.println("Throghout :" + nsPerTime + "ns/item");
            assertEquals(putSum.get(),takeSum.get());
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public static void main(String[] args)throws Exception{
        int tpt = 1000;
        for (int cap=1;cap<1000;cap*=10){
            System.out.println("cap:" + cap);
            for (int pairs = 1;pairs<=128;pairs*=2){
                TimedPutTakeTest t = new TimedPutTakeTest(cap,pairs,tpt);
                System.out.print("Pairs: " + pairs + "\t");
                t.test();
                System.out.print("\t");
                Thread.sleep(1000);
                t.test();
                System.out.println();
                Thread.sleep(1000);
            }
        }

    }

}
