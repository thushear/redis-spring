package net.shopin.concurrency;
/**
 * Memoizer1
 *
 * Initial cache attempt using HashMap and synchronization
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明:弱并发模型 使用 HashMap 同步整个compute 方法 导致性能下降
 * User: kongming
 * Date: 14-6-12
 * Time: 下午3:53
 */
public class Memoizer1<A,V> implements Computalbe<A,V> {

    @GuardedBy("this") private final Map<A,V> cache = new HashMap<A, V>();

    private final Computalbe<A,V> c;

    public Memoizer1(Computalbe<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if(result == null){
            result = c.compute(arg);
            cache.put(arg,result);
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

interface Computalbe<A,V>{
    V compute(A arg)throws InterruptedException;
}

class ExpensiveFunction implements Computalbe<String,BigInteger>{

    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        return new BigInteger(arg);
    }

}

