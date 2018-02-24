package yao.net.udt.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-4-19.
 */
public class CreateFrameHandler extends ByteToMessageDecoder {

    private static final int PACK_BYTE_MAX = 65555;//协议规定的最大字节数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("**channelActive** " + ctx.channel().remoteAddress() + "*()*" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("**channelInactive** " + ctx.channel().remoteAddress() + "*()*" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int size = in.readableBytes();
        String ch = in.readCharSequence(size, CharsetUtil.UTF_8).toString();
        System.out.println("xxx:"+ch);
        System.out.println(ch.equalsIgnoreCase("##12345"));
        if (ch.equalsIgnoreCase("##12345")){
            System.out.println("closed!");
//            ctx.close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        System.out.println("CreateFrameHandler异常信息：\r\n" + cause.getMessage());
    }

}
