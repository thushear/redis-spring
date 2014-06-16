package net.shopin.concurrency;
/**
 * SocketUsingTask
 * <p/>
 * Encapsulating nonstandard cancellation in a task with newTaskFor
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-16
 * Time: 下午8:17
 */
public abstract class SocketUsingTask<T> implements CancellableTask<T> {

    @GuardedBy("this") private Socket socket;

    protected synchronized void setSocket(Socket s){
        socket = s;
    }

    @Override
    public synchronized void cancel() {
        try {
            if (socket!=null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public RunnableFuture<T> newTask() {

        return new FutureTask<T>(this){};
    }
}

interface CancellableTask<T> extends Callable<T>{

    void   cancel();

    RunnableFuture<T> newTask();
}