package net.shopin.netty;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * from :　http://blog.csdn.net/kobejayandy/article/details/11493717
 *
 * 说明: Netty服务端
 * User: kongming
 * Date: 14-6-5
 * Time: 下午8:15
 */
public class NettyServer {

    public static void main(String[] args) {
        //server start
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool())
        );
        //设置一个处理客户端消息和各种消息事件的类 handler
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new NettyServerHandler());
            }
        });

        //开放8080 端口
        bootstrap.bind(new InetSocketAddress(8080));


    }


    public static  class NettyServerHandler extends SimpleChannelHandler{

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("Hello World , this is server");
        }
    }


}
