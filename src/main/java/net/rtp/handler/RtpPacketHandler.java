package net.rtp.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.rtp.message.RtpMessage;
import net.rtp.push.PushManager;
import net.rtp.push.PushTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * RTP 封包处理类
 *
 * @date 2018/7/16 0016 18:08
 */
public class RtpPacketHandler extends SimpleChannelInboundHandler<RtpMessage> {

    private Logger logger = LoggerFactory.getLogger(RtpPacketHandler.class);


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.debug(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        PushTask task = PushManager.get(ctx.name());
        if (task != null) {
            try {
                task.shutdown();
            } catch (Exception e) {
            }
            PushManager.remove(ctx.name());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RtpMessage rtpMessage) throws Exception {
        //logger.debug(rtpMessage.toString());
        System.out.println(rtpMessage);

        if (rtpMessage.getDataType() != 3 && rtpMessage.getDataType() != 4) {
            String taskName = rtpMessage.getSimNum() + "_" + rtpMessage.getLogicChnnel();
            PushTask task = PushManager.get(ctx.name());
            if (task == null) {
                task = PushManager.newPublishTask(ctx.name(), taskName);
                task.start();
            }
            task.write(rtpMessage.getBody());
            if (rtpMessage.getFlag() == 0 || rtpMessage.getFlag() == 2) {
                task.flush();
            }
        }
    }
}
