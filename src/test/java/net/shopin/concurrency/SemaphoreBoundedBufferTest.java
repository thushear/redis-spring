package net.shopin.concurrency;

import junit.framework.TestCase;
import net.shopin.util.StringUtil;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-25
 * Time: 下午1:38
 */
public class SemaphoreBoundedBufferTest extends TestCase{

    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;


    public void testIsEmptyWhenConstructed(){
        System.out.println("test init");
         SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
         assertFalse(bb.isFull());
    }


   public void testIsFullAfterPuts() throws InterruptedException {
       SemaphoreBoundedBuffer<Integer>  bb = new SemaphoreBoundedBuffer<Integer>(10);
       for (int i = 0;i < 10;i++)
           bb.put(i);
       assertTrue(bb.isFull());
       assertFalse(bb.isEmpty());
   }


    /**
     * 测试阻塞
     */
   public void testTakeBlocksWhenEmpty(){
       final SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
       Thread taker = new Thread(){

           @Override
           public void run() {
               try {
                   int unused = bb.take();
                   fail();//if we get here it is an error
               } catch (InterruptedException success) {
                   success.printStackTrace();
               }

           }
       };

       taker.start();
       try {
           Thread.sleep(10000);
           System.out.println(taker.isAlive());
           taker.interrupt();
           taker.join(LOCKUP_DETECT_TIMEOUT);
           assertFalse(taker.isAlive());
       } catch (InterruptedException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }



       /*try {
           taker.start();
           Thread.sleep(LOCKUP_DETECT_TIMEOUT);
           taker.interrupt();
           taker.join(LOCKUP_DETECT_TIMEOUT);
           assertFalse(taker.isAlive());
       } catch (InterruptedException e) {
           fail();
       }
*/
   }


    //--------------------------测试内存泄露

    /**
     * 巨型对象
     */
    class Big{
        double[] data = new double[100000];
    }

    public void testLeak() throws InterruptedException {
        SemaphoreBoundedBuffer<Big> bb = new SemaphoreBoundedBuffer<Big>(CAPACITY);
        long heapSize1 = snapshotHeap();
        for (int i = 0; i < CAPACITY; i++)
            bb.put(new Big());
        for (int i = 0; i < CAPACITY; i++)
            bb.take();
        long heapSize2 = snapshotHeap();
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }


    private long snapshotHeap() {
        try {
            StringUtil._runGC();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return StringUtil.usedMemory();
    }



}
