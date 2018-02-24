package yao.https.file;

/**
 * Created by Administrator on 2017-7-20.
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.PARTIAL_CONTENT;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;

public class HttpsFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;
    private ChannelHandlerContext ctx;
    private HttpRequest currentMessage;
    //正则匹配
    private static final Pattern CONTENT_RANGE_PATTERN = Pattern.compile("^bytes=(\\d+)-(\\d*)$");
    private static final Logger LOGGER = Loggers.getLogger(HttpsFileServerHandler.class);

    public HttpsFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FullHttpRequest request) throws Exception {
        this.ctx = ctx;
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        currentMessage = request;

//        final String uri = request.uri();
//        final String path = sanitizeUri(uri);
//        if (path == null) {
//            sendError(ctx, HttpResponseStatus.FORBIDDEN);
//            return;
//        }
//        File file = new File(path);
//        if (file.isHidden() || !file.exists()) {
//            sendError(ctx, HttpResponseStatus.NOT_FOUND);
//            return;
//        }
//        if (!file.isFile()) {
//            sendError(ctx, HttpResponseStatus.FORBIDDEN);
//        }
        //测试
        File file = new File("D:\\test\\ceshi\\a.png");
        //增加断点续传功能
        handleCenterRequest(request, file);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleCenterRequest(HttpRequest request, File file) throws IOException {
        String range = request.headers().get(HttpHeaderNames.RANGE);
        if (range != null) {
            partialContentResponse(range, request, file);
        } else {
            fullContentResponse(request, file);
        }
    }

    //分片下载
    private void partialContentResponse(String range, HttpRequest request, File file) throws IOException {
        Matcher matcher = CONTENT_RANGE_PATTERN.matcher(range);
        if (!matcher.matches()) {
            System.out.println("Invalid byte-range: {}; returning full content" + range);
            fullContentResponse(request, file);
            return;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        long start;
        long end;

        try {
            try {
                start = Long.parseLong(matcher.group(1));
                if (start > raf.length()) {
                    System.out.println("416 Requested Range not satisfiable");
                    simpleResponse(HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
                    raf.close();
                    return;
                }
                end = raf.length() - 1;
                if (!matcher.group(2).equals("")) {
                    end = Long.parseLong(matcher.group(2));
                }
            } catch (NumberFormatException ex) {
                System.out.println("Couldn't parse Range Header");
                start = 0;
                end = raf.length();
            }

            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, PARTIAL_CONTENT);
            maybeSetConnectionCloseHeader(response);
            HttpUtil.setContentLength(response, end - start + 1);
            response.headers().set(HttpHeaderNames.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + raf.length());
            setDefaultGetHeaders(response);

            ctx.channel().write(response);
            ChannelFuture writeFuture = transferFile(raf, start, end - start + 1);
            if (!HttpUtil.isKeepAlive(request)) {
                writeFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Throwable t) {
            /*
             * Make sure RandomAccessFile is closed when exception is raised.
             * In case of success, the ChannelFutureListener in "transferFile" will take care
             * that the resources are released.
             */
            raf.close();
            throw t;
        }

    }

    //全下载
    private void fullContentResponse(HttpRequest request, File file) throws IOException {
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        long len = accessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, len);
        setContentTypeHeader(response, file);
        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        ChannelFuture future;
        future = ctx.write(new ChunkedFile(accessFile, 0, len, 8192), ctx.newProgressivePromise());
        future.addListener(new ChannelProgressiveFutureListener() {

            @Override
            public void operationComplete(ChannelProgressiveFuture arg0)
                    throws Exception {
                System.out.println("Transfer complete.");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress,
                                            long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress:" + progress);
                } else {
                    System.err.println("Transfer progress:" + progress + "/" + total);
                }
            }
        });
        ChannelFuture lastfuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(request)) {
            lastfuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (Exception e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (Exception e2) {
                throw new Error();
            }
        }

        if (!uri.startsWith(url)) {
            return null;
        }

        if (!uri.startsWith("/")) {
            return null;
        }

        uri = uri.replace('/', File.separatorChar);
        if (uri.contains('.' + File.separator) || uri.startsWith(".")
                || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        return System.getProperty("user.dir") + File.separator + uri;
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private static void sendRedirect(ChannelHandlerContext ctx, String newuri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newuri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); //异步发送 发送完成后就关闭连接
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, typeMap.getContentType(file.getPath()));
    }

    private void simpleResponse(HttpResponseStatus status, String body) {
        if (body == null) {
            simpleResponse(status);
            return;
        }
        if (!body.endsWith("\n")) {
            body += "\n";
        }
        ByteBuf content = ByteBufUtil.writeUtf8(ctx.alloc(), body);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        HttpUtil.setContentLength(response, body.length());
        maybeSetConnectionCloseHeader(response);
        sendResponse(response);
    }

    private void maybeSetConnectionCloseHeader(HttpResponse response) {
        if (currentMessage == null || !HttpUtil.isKeepAlive(currentMessage)) {
            response.headers().set(HttpHeaderNames.CONNECTION, "close");
        }
    }

    private void sendResponse(HttpResponse response) {
        ChannelFuture cf = ctx.channel().writeAndFlush(response);
        if (currentMessage != null && !HttpUtil.isKeepAlive(currentMessage)) {
            cf.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private HttpResponse prepareResponse(HttpResponseStatus status) {
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        HttpUtil.setContentLength(response, 0);
        maybeSetConnectionCloseHeader(response);
        return response;
    }

    private void simpleResponse(HttpResponseStatus status) {
        sendResponse(prepareResponse(status));
    }

    private static final String EXPIRES_VALUE = "Thu, 31 Dec 2037 23:59:59 GMT";
    private static final String CACHE_CONTROL_VALUE = "max-age=315360000";

    private void setDefaultGetHeaders(HttpResponse response) {
        response.headers().set(HttpHeaderNames.ACCEPT_RANGES, "bytes");
        response.headers().set(HttpHeaderNames.EXPIRES, EXPIRES_VALUE);
        response.headers().set(HttpHeaderNames.CACHE_CONTROL, CACHE_CONTROL_VALUE);
    }

    private ChannelFuture transferFile(RandomAccessFile raf, long position, long count)
            throws IOException {

        Channel channel = ctx.channel();
        final ChannelFuture fileFuture;
        final ChannelFuture endMarkerFuture;

        FileRegion region = new DefaultFileRegion(raf.getChannel(), position, count);
        fileFuture = channel.write(region, ctx.newProgressivePromise());
        // Flushes and sets the ending marker
        endMarkerFuture = channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        fileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                LOGGER.trace("transferFile progress={} total={}", progress, total);
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                LOGGER.trace("transferFile operationComplete");
            }
        });

        return endMarkerFuture;
    }

}
