package com.tby.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface IPathHandler {
    void handleRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception;
}
