package test.nettyserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.ogcs.netty.impl.TcpProtocolClient;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    private static final ChannelHandler FRAME_PREPENDER = new DelimiterBasedFrameDecoder(65535, Unpooled.copiedBuffer(new byte[]{0x23}),
            Unpooled.copiedBuffer(new byte[]{0x24, 0x23}), Unpooled.copiedBuffer(new byte[]{0x24}));

    private static final ChannelHandler GPB_DECODER_HANDLER = new StringDecoder();

    private static final ChannelHandler GPB_ENCODER_HANDLER = new StringEncoder();

    public static void main(String[] args) {

        TcpProtocolClient client = new TcpProtocolClient("192.168.90.107", 9008) {
            @Override
            protected ChannelHandler newChannelInitializer() {
                return new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();
                        cp.addLast("prepender", FRAME_PREPENDER);
                        cp.addLast("decoder", GPB_DECODER_HANDLER);
                        cp.addLast("encoder", GPB_ENCODER_HANDLER);
                        // handler
                        cp.addLast("handler", new SimpleChannelInboundHandler<String>() {

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

                                System.out.println("msg:" + msg);

                            }
                        });
//                cp.addLast("handler", new ServerHandler());
                    }
                };
            }
        };
        client.start();

        AtomicInteger ID = new AtomicInteger(0);
        Channel client1 = client.client();
        client1.writeAndFlush(ID.toString());
    }
}
