package com.snakechant.app.websocket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    private static final Logger logger
            = LoggerFactory.getLogger(IdleHandler.class);

    @Override  
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        logger.info("receive binary message");
        msg.retain();
        ctx.writeAndFlush(msg);
    }  
}