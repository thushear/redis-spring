package net.shopin.concurrency;
/**
 * TimedRun2
 * <p/>
 * Interrupting a task in a dedicated thread
 *
 * @author Brian Goetz and Tim Peierls
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 下午4:24
 */
public class TimedRun2 {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timeRun(final Runnable r,long timeout,TimeUnit unit) throws InterruptedException {

        class RethrowableTask implements Runnable{
            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow(){
                if(t!=null)
                    throw LaunderThrowable.launderThrowable(t);
            }
        }
        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable(){

            @Override
            public void run() {
                taskThread.interrupt();
            }
        },timeout,unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }


    public static void main(String[] args) {

      /*  cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("run");

            }
        },1,TimeUnit.SECONDS);
*/
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("run");
            }
        };
        try {
            timeRun(r,2,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        cancelExec.shutdownNow();

    }


}
