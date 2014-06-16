package net.shopin.concurrency;
/**
 * BrokenPrimeProducer
 * <p/>
 * Unreliable cancellation that can leave producers stuck in a blocking operation
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 下午3:54
 */
public class BrokenPrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    private volatile boolean cancelled = false;


    BrokenPrimeProducer(BlockingQueue queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;

        try {
            while (!cancelled)
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void cancel(){
        cancelled = true;
    }
}
