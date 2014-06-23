package net.shopin.concurrency;
/**
 * LogWriter
 * <p/>
 * Producer-consumer logging service with no shutdown support
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 说明:生产者-消费者日志服务--不支持关闭
 * User: kongming
 * Date: 14-6-17
 * Time: 上午10:33
 */
public class LogWriter {

    private final BlockingQueue<String> queue;

    private final LoggerThread logger;

    private static final int CAPACITY = 1000;

    public LogWriter(Writer writer){
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start(){
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }


    private class LoggerThread extends Thread{
        private final PrintWriter writer;

        public LoggerThread(Writer writer){
             this.writer = new PrintWriter(writer,true);//autoflush
        }

        @Override
        public void run() {
            try {
                while (true)
                    writer.println(queue.take());
            } catch (InterruptedException e) {

            } finally {
                writer.close();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Writer write = new PrintWriter(System.out);
        LogWriter log = new LogWriter(write);
        int i =0;
        while(i<10){
            log.log("producer" + i);
            i++;
        }

        log.start();

    }


}
