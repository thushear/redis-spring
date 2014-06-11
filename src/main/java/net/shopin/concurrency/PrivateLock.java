package net.shopin.concurrency;

/**
 * PrivateLock
 * <p/>
 * Guarding state with a private lock
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午4:22
 */
public class PrivateLock {

    private final Object myLock = new Object();

    void someMethod(){
        synchronized (myLock){
            //私有锁保护
        }
    }

}
