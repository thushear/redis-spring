package net.shopin.concurrency;
/**
 * MyThreadFactory
 * <p/>
 * Custom thread factory
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.ThreadFactory;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-18
 * Time: 下午3:09
 */
public class MyThreadFactory implements ThreadFactory{

    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new MyAppThread(r,poolName);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
