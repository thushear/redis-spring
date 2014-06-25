package net.shopin.concurrency;

import junit.framework.TestCase;

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


}
