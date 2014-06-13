package net.shopin.concurrency;
/**
 * TaskExecutionWebServer
 * <p/>
 * Web server using a thread pool
 *
 * @author Brian Goetz and Tim Peierls
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 说明:使用Executor 框架的线程池 实现的web server
 * User: kongming
 * Date: 14-6-13
 * Time: 上午11:43
 */
public class TaskExecutionWebServer {

    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8002);
        while (true){
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                   handleRequest(connection);
                }
            };
            exec.execute(task);
        }


    }


    private static void handleRequest(Socket connetion) {
        System.out.println("open socket");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(connetion.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        writer.write("thread pool web server demo");
        writer.flush();
        writer.close();
        return;
    }

}
