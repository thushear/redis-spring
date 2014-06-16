package net.shopin.concurrency;
/**
 * ReaderThread
 * <p/>
 * Encapsulating nonstandard cancellation in a Thread by overriding interrupt
 *
 * @author Brian Goetz and Tim Peierls
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 说明:通过覆写interrupt 来封装非标准取消
 * User: kongming
 * Date: 14-6-16
 * Time: 下午7:24
 */
public class ReaderThread extends Thread{

    private static final int BUFSZ = 512;

    private final Socket socket;

    private final InputStream in;

    public ReaderThread( Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        try {
            System.out.println("unstandard cancel close socket");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {

        try {
            byte[] buf = new byte[BUFSZ];
            while (true){
                int count = in.read(buf);
                if(count < 0)
                    break;
                else if(count > 0)
                    processBuffer(buf,count);

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void processBuffer(byte[] buf, int count) {

    }


    public static void main(String[] args) {
        try {
            ReaderThread t =  new ReaderThread(new Socket("192.168.200.167",80));
            t.start();
            t.interrupt();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
