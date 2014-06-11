package net.shopin.concurrency;

/**
 * ListHelder
 * <p/>
 * Examples of thread-safe and non-thread-safe implementations of
 * put-if-absent helper methods for List
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午6:01
 */
public class ListHelper {

    /**
     * 助手类加的锁和父类List的锁不是同一把锁 起不到作用
     * @param <E>
     */
    @NotThreadSafe
    class BadListHelper<E>{
        public List<E> list = Collections.synchronizedList(new ArrayList<E>());

        public synchronized boolean pushIfAbsent(E x){
            boolean absent = !list.contains(x);
            if(absent)
                list.add(x);
            return absent;
        }

    }


    /**
     * 助手类正确的枷锁方式
     * @param <E>
     */
    @ThreadSafe
    class GoodListHelper<E>{
        public List<E> list = Collections.synchronizedList(new ArrayList<E>());

        public boolean pushIfAbsent(E x){
            synchronized (list){
                boolean absent = !list.contains(x);
                if(absent)
                    list.add(x);
                return absent;

            }

        }


    }


}
