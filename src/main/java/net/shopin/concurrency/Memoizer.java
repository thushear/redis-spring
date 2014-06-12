package net.shopin.concurrency;
/**
 * Memoizer
 * <p/>
 * Final implementation of Memoizer
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.*;

/**
 * 说明: perfect 缓存包装器 Wrapper
 * User: kongming
 * Date: 14-6-12
 * Time: 下午4:59
 */
public class Memoizer<A, V> implements Computalbe<A, V> {

    private final ConcurrentMap<A, Future<V>> cache =
            new ConcurrentHashMap<A, Future<V>>();
    private final Computalbe<A, V> c;

    public Memoizer(Computalbe<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);  //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                //利用ConcurrentMap 的原子性的PushIfAbsent 弥补了并发重复计算的缺陷
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
