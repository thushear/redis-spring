package net.shopin.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-6
 * Time: 下午1:04
 */
public class MessageClient {

    public static void main(String[] args) {

        //client server
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool())
        );
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new MessageClientHandler());
            }
        });
        //connect 8000
        bootstrap.connect(new InetSocketAddress("127.0.0.1",8000));



    }

    private static class MessageClientHandler extends SimpleChannelHandler{

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            //
            System.out.println("netty client");
            String msg = "hello netty";
            ChannelBuffer buffer = ChannelBuffers.buffer(msg.length());
            buffer.writeBytes(msg.getBytes());
            e.getChannel().write(buffer);
        }
    }


}
