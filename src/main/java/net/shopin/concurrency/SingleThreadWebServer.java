package net.shopin.concurrency;
/**
 * SingleThreadWebServer
 * <p/>
 * Sequential web server
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 说明:顺序化执行模型的web server
 * User: kongming
 * Date: 14-6-13
 * Time: 上午11:00
 */
public class SingleThreadWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8000);
        while (true){
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        try {
            PrintWriter write = new PrintWriter(connection.getOutputStream());
            write.println("Sequential web server");
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
