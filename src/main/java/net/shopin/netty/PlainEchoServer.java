package net.shopin.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 说明:  bio server
 * User: kongming
 * Date: 14-6-9
 * Time: 上午9:50
 */
public class PlainEchoServer {


    public static void main(String[] args) {
        try {
            new PlainEchoServer().serve(8080);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        while(true){
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(),true);
                        //while(reader.readLine()!=null){
                            writer.println(reader.readLine());
                            writer.println("hello bio server ");
                            writer.flush();
                        //}
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        try {
                            clientSocket.close();
                        } catch (IOException e1) {
                        }
                    }
                }
            }).start();
        }
    }
}