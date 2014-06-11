package net.shopin.concurrency;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-11
 * Time: 下午6:10
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * ImprovedList
 *
 * Implementing put-if-absent using composition
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ImprovedList<T> implements List<T> {

    private final List<T> list;

    public ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean pushIfAbsent(T x){
        boolean contains = list.contains(x);
        if(contains)
            list.add(x);
        return  !contains;
    }


    @Override
    public int size() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean contains(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Iterator<T> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean add(T t) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T get(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T set(int index, T element) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void add(int index, T element) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T remove(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int indexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
