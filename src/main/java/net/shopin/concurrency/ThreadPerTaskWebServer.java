package net.shopin.concurrency;
/**
 * ThreadPerTaskWebServer
 * <p/>
 * Web server that starts a new thread for each request
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 说明:每任务每线程模型 web server
 * User: kongming
 * Date: 14-6-13
 * Time: 上午11:12
 */
public class ThreadPerTaskWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8001);
        while (true){
            final Socket connetion = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connetion);
                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter(connetion.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    writer.write("hh");
                    writer.flush();
                    writer.close();
                }


            };
            new Thread(task).start();
        }
    }

    private static void handleRequest(Socket connetion) {
        System.out.println("open socket");
        return;
    }

}
