package net.shopin.concurrency;

/**
 * Counter
 * <p/>
 * Simple thread-safe counter using the Java monitor pattern
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午4:01
 */
@ThreadSafe
public final class Counter {
    @GuardedBy("this") private long value = 0;

    public synchronized long increment(){
        if(value == Long.MAX_VALUE)
            throw new IllegalStateException("Counter overflow");
        return ++value;
    }

    public synchronized   long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
