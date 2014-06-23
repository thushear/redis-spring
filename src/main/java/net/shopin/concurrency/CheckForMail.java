package net.shopin.concurrency;
/**
 * CheckForMail
 * <p/>
 * Using a private \Executor whose lifetime is bounded by a method call
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 说明:使用私有的Executor 使得他的寿命限定于一次方法调用
 * User: kongming
 * Date: 14-6-17
 * Time: 下午5:00
 */
public class CheckForMail {

    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }

    public boolean checkMail(Set<String> hosts,long timeout,TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for(final String host : hosts){
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                       if(checkMail(host))
                             hasNewMail.set(true);
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout,unit);
        }

        return hasNewMail.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Set set = new HashSet();
        set.add("127.0.0.1");
        new CheckForMail().checkMail(set,10,TimeUnit.SECONDS);
    }

}
