package net.shopin.concurrency;
/**
 * LeftRightDeadlock
 *
 * Simple lock-ordering deadlock
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明:偶发时序下会发生 顺序锁
 * User: kongming
 * Date: 14-6-20
 * Time: 上午10:56
 */
public class LeftRightDeadlock {


    private final static LeftRightDeadlock deadlock = new LeftRightDeadlock();

    private final Object left = new Object();

    private final Object right = new Object();

    public final void leftRight(){
        synchronized (left){
            synchronized (right){
                System.out.println("left right lock");
            }
        }

    }

    public void rightLeft(){
        synchronized (right){
            synchronized (left){
                System.out.println("right left lock");
            }
        }
    }

    public static   class Lock implements Runnable{

        private final int flag;


        public Lock(int flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            if(flag == 0)
                deadlock.leftRight();
            else
                deadlock.rightLeft();
        }
    }


    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();

    }


}
