package com.tby.http.server;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
    Map<String, IPathHandler> handlerMap = new HashMap<>();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        //处理http服务的关键handler
        ph.addLast("encoder", new HttpResponseEncoder());
        ph.addLast("decoder", new HttpRequestDecoder());
        ph.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        ph.addLast("handler", new NettyServerHandler());// 服务端业务逻辑
    }

    public synchronized void addPathHandler(String path, IPathHandler handler) {
        handlerMap.put(path, handler);
    }

    public class NettyServerHandler extends ChannelInboundHandlerAdapter {
        private String result = "";
        /*
         * 收到消息时，返回信息
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!(msg instanceof FullHttpRequest)) {
                result = "未知请求!";
                HttpServerUtils.send(ctx, result, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            try {
                String path = httpRequest.uri();          //获取路径
                //如果不是这个路径，就直接返回错误
                IPathHandler handler = handlerMap.get(path);
                if (handler != null) {
                    handler.handleRequest(ctx, httpRequest);
                } else {
                    result = "404 Not Found!";
                    HttpServerUtils.send(ctx, result, HttpResponseStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                result = "服务器错误!";
                HttpServerUtils.send(ctx, result, HttpResponseStatus.INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            } finally {
                //释放请求
                httpRequest.release();
            }
        }

        /*
         * 建立连接时，返回消息
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
            ctx.writeAndFlush("客户端" + InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
            super.channelActive(ctx);
        }
    }
}