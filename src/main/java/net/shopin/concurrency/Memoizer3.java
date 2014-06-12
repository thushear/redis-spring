package net.shopin.concurrency;

/**
 * Memoizer3
 * <p/>
 * Memoizing wrapper using FutureTask
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.Map;
import java.util.concurrent.*;

/**
 * 说明:仍然存在偶然时序下重复计算的问题 --唯一的缺陷
 * User: kongming
 * Date: 14-6-12
 * Time: 下午4:21
 */
public class Memoizer3<A,V> implements Computalbe<A,V>{


    private final Map<A,Future<V>> cache =  new ConcurrentHashMap<A, Future<V>>();

    private final Computalbe<A,V> c;

    public Memoizer3(Computalbe<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        //利用FutureTask 的好处是可以获得另一个线程尚未计算完成的结果 这是Futrue的nb之处
        Future<V> f = cache.get(arg);
        if(f == null){
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg,ft);
            ft.run();//call to c.compute happens here
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e);
        }
    }

}
