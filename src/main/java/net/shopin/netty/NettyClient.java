package net.shopin.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 说明:   client
 * User: kongming
 * Date: 14-6-5
 * Time: 下午8:25
 */
public class NettyClient {


    public static void main(String[] args) {

        //client 服务启动
        ClientBootstrap bootstrap = new ClientBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        //设置一个处理服务端消息和各种消息事件的类
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new NettyClientHandler());
            }
        });

        bootstrap.connect(new InetSocketAddress("127.0.0.1",8080));

    }


    public static class NettyClientHandler extends SimpleChannelHandler{

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("hello world , im client");
        }
    }



}
