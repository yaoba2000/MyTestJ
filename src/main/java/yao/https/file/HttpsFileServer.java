package yao.https.file;

/**
 * Created by Administrator on 2017-7-20.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import yao.http.utils.SecureChatSslContextFactory;

import javax.net.ssl.SSLEngine;

public class HttpsFileServer {
    private static final String DEFAULT_URL = "/source/";


    public void run(final int port, final String url) throws Exception {

//        final SslContext sslCtx;
//        SelfSignedCertificate ssc = new SelfSignedCertificate();
//        //具体场景要通过文件
//        sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        boolean epoll = Epoll.isAvailable();
        System.out.println("epoll:" + epoll);
        EventLoopGroup bossGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        EventLoopGroup workerGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http_decoder", new HttpRequestDecoder())
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                                    .addLast("http-encoder", new HttpResponseEncoder())
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    .addLast("fileserverhandler", new HttpsFileServerHandler(url));
                        }
                    });

            ChannelFuture f = b.bind(port).sync();

            System.out.println("HTTP File Server Start..  http://localhost:" + port + url);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new HttpsFileServer().run(7777, DEFAULT_URL);
    }
}
