package net.shopin.concurrency;

/**
 * StripedMap
 * <p/>
 * Hash-based map using lock striping
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明: 模仿ConcurrentHashMap 实现分离锁的原理 分离锁 减小压力
 * User: kongming
 * Date: 14-6-24
 * Time: 下午4:03
 */
public class StripedMap {

    // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;


    private static class Node{
        Node next;
        Object key;
        Object value;
    }


    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for(int i = 0;i<N_LOCKS;i++)
            locks[i] = new Object();
    }

    private final int hash(Object key){
        return Math.abs(key.hashCode()%buckets.length);
    }

    public Object get(Object key){
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]){
            for (Node m = buckets[hash];m!=null;m=m.next)
                if(m.key.equals(key))
                    return m.value;
        }
        return null;
    }


    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i % N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }

}
