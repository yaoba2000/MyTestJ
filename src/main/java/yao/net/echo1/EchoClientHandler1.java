package yao.net.echo1;

/**
 * Created by Administrator on 2017-5-17.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import yao.util.HandleUtil;

import java.net.InetSocketAddress;

//L
public class EchoClientHandler1 extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
            throws Exception {
        //服务器推送对方IP和PORT
        ByteBuf buf = (ByteBuf) packet.copy().content();
        System.out.println(ctx.channel()+" "+ByteBufUtil.hexDump(buf));
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String str = new String(req, "UTF-8");
        String[] list = str.split(" ");
        //如果是A 则发送
        System.out.println("接收到的信息:" + ctx.channel().toString() + " " + str);
        if (list[0].equals("A")) {
            String ip = list[1];
            String port = list[2];
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("打洞信息".getBytes()), new InetSocketAddress(ip, Integer.parseInt(port))));
            Thread.sleep(1000);
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("P2P info..from client1".getBytes()), new InetSocketAddress(ip, Integer.parseInt(port))));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端1向服务器发送自己的IP和PORT");
        ctx.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(HandleUtil.HexStringToBytes("5443505301FE053132333435010006125850FBA83EBB")),//"L".getBytes()),
//                new InetSocketAddress("183.1.1.1", 7402)));
                new InetSocketAddress("192.168.90.179", 8907)));
        super.channelActive(ctx);
    }
}