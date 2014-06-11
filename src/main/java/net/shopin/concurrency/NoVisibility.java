package net.shopin.concurrency;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-10
 * Time: 下午5:15
 */
public class NoVisibility {

    private static boolean   ready;
    private static int number;

    private static class ReaderThread extends Thread{

        @Override
        public void run() {
            while(!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }



}
