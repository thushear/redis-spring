package net.shopin.concurrency;
/**
 * LogService
 * <p/>
 * Adding reliable cancellation to LogWriter
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 说明:带有关闭功能的LogWriter
 * User: kongming
 * Date: 14-6-17
 * Time: 下午3:46
 */
public class LogService {

    private final BlockingQueue<String> queue;

    private final PrintWriter writer;

    private final LoggerThread loggerThread;

    @GuardedBy("this") boolean isShutDown;

    @GuardedBy("this") int reservations;


    public LogService(Writer writer){
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start(){
        loggerThread.start();
    }


    public void log(String msg) throws InterruptedException {
        synchronized (this){
            if(isShutDown)
                throw new IllegalStateException();
            ++reservations;
        }
        queue.put(msg);
    }

    public void stop(){
        synchronized (this){
            isShutDown = true;
        }
        loggerThread.interrupt();
    }


    private class LoggerThread extends Thread{

        @Override
        public void run() {
            try {
                while (true){

                    synchronized (LogService.this){
                        if(isShutDown&&reservations==0)
                            break;
                    }
                    String msg = queue.take();
                    synchronized (LogService.this){
                        --reservations;
                    }
                    writer.println(msg);
                }
            } catch (InterruptedException e) {
                //retry
                if(reservations!=0)
                    run();
            }finally {
                writer.close();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Writer writer = new PrintWriter(System.out);
        LogService logService = new LogService(writer);
        logService.start();
        for(int i = 0;i<10;i++)
            logService.log("test producer " + i);

        logService.stop();
    }

}
