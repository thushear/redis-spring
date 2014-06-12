package net.shopin.concurrency;
/**
 * Memoizer2
 * <p/>
 * Replacing HashMap with ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 说明:比HashMap提供了更好的并发性,依赖于ConcurrentHashMap
 *  但是并未解决重复计算问题
 * User: kongming
 * Date: 14-6-12
 * Time: 下午4:14
 */
public class Memoizer2<A,V> implements Computalbe<A,V> {

    private final Map<A,V>  cache = new ConcurrentHashMap<A, V>();

    private final Computalbe<A,V> c;

    public Memoizer2(Computalbe<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if(result==null){
            result = c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }

}
