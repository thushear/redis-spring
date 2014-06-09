package net.shopin.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 说明:
 * User: kongming
 * Date: 14-6-9
 * Time: 下午1:58
 */
public class NettyEchoClient {

    private final String host;
    private final int port;

    public NettyEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start()throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture  f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }


    @ChannelHandler.Sharable
    public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.write(Unpooled.copiedBuffer("netty rocks!", CharsetUtil.UTF_8));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            System.out.println("client received :" + ByteBufUtil.hexDump(byteBuf.readBytes(byteBuf.readableBytes())));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }


    public static void main(String[] args) throws Exception {
        new NettyEchoClient("127.0.0.1",8001).start();
    }

}
