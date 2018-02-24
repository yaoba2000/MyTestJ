package yao.net.echo.test;

/**
 * Created by Administrator on 2017-5-17.
 */
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class EchoServerTest {



    public static void main(String[] args) {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new LoggingHandler(LogLevel.INFO));
//                    .handler(new EchoServerHandler());

//            b.bind(7402).sync().channel().closeFuture().await();
            Channel ch =  b.bind(8007).sync().await().channel();
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("192.168.90.107"), 5777);
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("TTT".getBytes()),addr));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } finally{
//            group.shutdownGracefully();
//        }

    }
}