package net.shopin.concurrency;
/**
 * PrimeGenerator
 * <p/>
 * Using a volatile field to hold cancellation state
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 上午10:26
 */
@ThreadSafe
public class PrimeGenerator implements Runnable{

    private static ExecutorService exec  = Executors.newCachedThreadPool();

    @GuardedBy("this") final List<BigInteger> primes = new ArrayList<BigInteger>();

    private volatile boolean cancelled;

    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled){
           p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }


    public void cancel(){
        cancelled = true;
    }

    public synchronized List<BigInteger> get(){
        return  new ArrayList<BigInteger>(primes);
    }

    static List<BigInteger> aSecondOfPrimes()throws InterruptedException{
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);

        try {
            TimeUnit.SECONDS.sleep(10);
        } finally {
            generator.cancel();
        }
        return generator.get();

    }

    public static void main(String[] args) throws InterruptedException {
        ;
        System.out.println(aSecondOfPrimes());
    }

}
