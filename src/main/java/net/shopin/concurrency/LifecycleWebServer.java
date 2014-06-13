package net.shopin.concurrency;


/**
 * LifecycleWebServer
 * <p/>
 * Web server with shutdown support
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 说明:  支持生命周期的web server 可扩展支持平滑stop
 * User: kongming
 * Date: 14-6-13
 * Time: 下午1:52
 */
public class LifecycleWebServer {

    private final ExecutorService exec = Executors.newCachedThreadPool();

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(8003);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(conn);
                        try {
                            InputStream input = conn.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                            System.out.println("open socket");
                            PrintWriter writer = null;
                            writer = new PrintWriter(conn.getOutputStream());
                            writer.write("thread pool web server demo");
                            String line = reader.readLine();
                            /*while (line!=null){
                                writer.println(line);
                                writer.flush();
                                line = reader.readLine();
                            }*/

                            writer.println(line);

                            writer.close();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown())
                    log("task mission rejected", e);
            }

        }

    }


    void handleRequest(Socket conn) {
        Request req = readRequest(conn);
        if (isShutdownRequest(req))
            stop();
        else
            dispatchRequest(req);
    }

    private void dispatchRequest(Request r) {

    }

    private boolean isShutdownRequest(Request r) {
        return false;
    }

    private Request readRequest(Socket s) {
        return null;
    }


    public void stop() {
        exec.shutdown();
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }

    interface Request {
    }


    public static void main(String[] args) throws IOException {
        new LifecycleWebServer().start();
    }

}
