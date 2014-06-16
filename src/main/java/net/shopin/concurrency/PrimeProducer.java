package net.shopin.concurrency;
/**
 * PrimeProducer
 * <p/>
 * Using interruption for cancellation
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 下午4:00
 */
public class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    public PrimeProducer( BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());

        } catch (Exception e) {

        }

    }

    public void cancel(){
        interrupt();
    }


    public static void main(String[] args) {






    }


}
