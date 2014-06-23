package net.shopin.concurrency;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * TrackingExecutor
 * <p/>
 * ExecutorService that keeps track of cancelled tasks after shutdown
 *
 * @author Brian Goetz and Tim Peierls
 */
/**
 * 说明:在终止时获取正在进行尚未结束的线程列表
 * User: kongming
 * Date: 14-6-17
 * Time: 下午5:20
 */
public class TrackingExecutor  extends AbstractExecutorService{

    private final ExecutorService exec;

    private final Set<Runnable> tasksCancelledAtShutDown =
            Collections.synchronizedSet(new HashSet<Runnable>());

    public TrackingExecutor(ExecutorService exec){
        this.exec = exec;
    }


    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShutdown() {
        return exec.isShutdown();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isTerminated() {
        return exec.isTerminated();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout,unit);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void execute(final Runnable command) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    if(isShutdown()&&Thread.currentThread().isInterrupted())
                        tasksCancelledAtShutDown.add(command);
                }
            }
        });
    }

    public List<Runnable> getCancelledTasks(){
        if(!exec.isTerminated())
            throw new IllegalStateException();
        return  new ArrayList<Runnable>(tasksCancelledAtShutDown);
    }


}
