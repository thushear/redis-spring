package net.shopin.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

/**
 * 说明:  发送ChannelBUffer 测试
 * User: kongming
 * Date: 14-6-6
 * Time: 上午11:35
 */
public class MessageServer {


    public static void main(String[] args) {
        // Server服务启动器
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool())
        );
        //set handler for server
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new MessageServerHandler());
            }
        });
        bootstrap.bind(new InetSocketAddress("127.0.0.1",8000));

    }

    private static class MessageServerHandler extends SimpleChannelHandler{
        /**
         *
         * @param ctx
         * @param e
         * @throws Exception
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            System.out.println("netty server ");
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            System.out.println(buffer.toString(Charset.defaultCharset()));
        }
    }


}
