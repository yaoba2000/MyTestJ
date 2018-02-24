/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package yao.net.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import yao.net.udt.handler.CreateFrameHandler;

import java.net.InetSocketAddress;

import static yao.net.udt.ByteEchoServer.kv;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class ByteEchoServerHandler extends ChannelInboundHandlerAdapter {

    InetSocketAddress addr1 = null;
    InetSocketAddress addr2 = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        System.err.println("ECHO active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
//        ctx.write(msg);
//        ByteBuf in = (ByteBuf) msg;
//        int size = in.readableBytes();
//        String ch = in.readCharSequence(size, CharsetUtil.UTF_8).toString();
//        System.out.println("ch:" + ch);
//        if (ch.equals("A")) {
//            System.out.println("send to A.");
//            kv.put(ch, ctx.channel());
//            ctx.writeAndFlush("AA".getBytes());
//        }
//        if (ch.equals("B")){
//            System.out.println("send to b.");
//            kv.put(ch, ctx.channel());
//            ctx.writeAndFlush("BB".getBytes());
//        }
//        if (ch.equals("C")) {
//            kv.get("A").writeAndFlush("BB".getBytes());
//            kv.get("B").writeAndFlush("AA".getBytes());
//        }
//        ReferenceCountUtil.release(msg);
        ByteBuf in = (ByteBuf) msg;
        int size = in.readableBytes();
        System.out.println("size1:"+size);
        if (size < 2) {
            return;
        }
        in.markReaderIndex();
        String ch = in.readCharSequence(2, CharsetUtil.UTF_8).toString();
        System.out.println(ch);
        if ("##".equals(ch)) {//新能源电动车协议
            ctx.pipeline().addLast("CreateFrameHandler", new CreateFrameHandler());
            ctx.pipeline().remove(this);
        } else {
            ctx.fireChannelRead(msg);
        }
        in.resetReaderIndex();
        ctx.fireChannelActive();//传给下一个fireChannelActive();
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
