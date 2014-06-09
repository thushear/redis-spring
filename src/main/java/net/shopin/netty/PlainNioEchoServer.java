package net.shopin.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 说明:  NIO server
 * User: kongming
 * Date: 14-6-9
 * Time: 上午10:25
 */
public class PlainNioEchoServer {


    public void serve(int port)throws IOException {
        System.out.println("listening for connections on port " + port);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);
        serverChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                break;
            }
            Set readyKeys = selector.selectedKeys();
            Iterator iter = readyKeys.iterator();
            while (iter.hasNext()){
                SelectionKey key = (SelectionKey) iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    System.out.println("Accept conection from " + client);
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ, ByteBuffer.allocate(100));

                }
                if(key.isReadable()){
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer output = (ByteBuffer) key.attachment();
                    client.read(output);
                }
                if(key.isWritable()){
                    SocketChannel client  = (SocketChannel) key.channel();
                    ByteBuffer output = (ByteBuffer) key.attachment();
                    output.flip();
                    client.write(output);
                    output.compact();

                }

            }
        }

    }


    public static void main(String[] args) {
        try {
            new PlainNioEchoServer().serve(8000);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }



}
