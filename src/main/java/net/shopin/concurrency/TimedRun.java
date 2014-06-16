package net.shopin.concurrency;
/**
 * TimedRun
 * <p/>
 * Cancelling a task using Future
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.util.concurrent.*;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 下午7:02
 */
public class TimedRun {

    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timeRun(Runnable r,long timeout,TimeUnit unit){
        Future<?> task = taskExec.submit(r);

        try {
            task.get(timeout,unit);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        } catch (TimeoutException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }finally {
            //如果任务已结束,是无害的
            task.cancel(true);//interrupt if running
        }

    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable run ... rabbit run run ");
            }
        };
        timeRun(r,2,TimeUnit.SECONDS);

        taskExec.shutdownNow();
    }



}
