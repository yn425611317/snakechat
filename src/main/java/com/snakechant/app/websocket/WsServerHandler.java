package com.snakechant.app.websocket;

import com.snakechant.app.Start;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class WsServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger
            = LoggerFactory.getLogger(IdleHandler.class);

    //the line of clients
    public static final ConcurrentHashMap<String, ChannelGroup> clientGroup = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, ChannelId> clients = new ConcurrentHashMap<>();
    private AttributeKey<String> requestUrl = AttributeKey.valueOf("url");




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    // url:type:id@  type 1 single chat 2 group chat
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String channel = ctx.channel().attr(requestUrl).get();
        logger.info("com.snakechant.app.websocket url: {}", channel);
        String text = msg.text();
        logger.info("receive message is {}", text);
        try {
            int i = text.indexOf("@");
            String protocolHead = text.substring(0, i);
            String[] split = protocolHead.split(":");
            String type = split[0];
            if (clientGroup.containsKey(channel.trim())) {
                //single chat
                if (type.equals(ChatType.SINGLE.getType())) {
                    //group chat
                    Channel channel1 = clientGroup.get(channel).find(clients.get(split[1]));
                    channel1.writeAndFlush(new TextWebSocketFrame("server peer: " + text.substring(i + 1)));
                } else if (type.equals(ChatType.GROUP.getType())) {
                    clientGroup.get(channel).writeAndFlush(new TextWebSocketFrame("server: " + text.substring(i + 1)));

                }
            }

        } catch (Exception e) {

            ctx.writeAndFlush(new TextWebSocketFrame("not support message protocol"));
        }


    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //successed handshake then add this channel to a map
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String requestUri = handshakeComplete.requestUri();
            WsServerHandler.clientGroup.putIfAbsent(requestUri, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            String id = ctx.channel().id().asLongText().replaceAll("-", "");
            WsServerHandler.clientGroup.computeIfPresent(requestUri, (key, value) -> {
                value.add(ctx.channel());
                return value;
            });
            WsServerHandler.clients.put(id, ctx.channel().id());
            WsServerHandler.clientGroup.get(requestUri)
                    .writeAndFlush(new TextWebSocketFrame(id + ": line"));


            ctx.channel().attr(requestUrl).set(requestUri);
            logger.info("requestUri:[{}]", requestUri);
            String subproTocol = handshakeComplete.selectedSubprotocol();
            logger.info("subproTocol:[{}]", subproTocol);
//            handshakeComplete.requestHeaders().forEach(entry -> logger.info("header key:[{}] value:[{}]", entry.getKey(), entry.getValue()));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        String s = ctx.channel().id().asLongText();
        clients.remove(s.replaceAll("-", ""));
        logger.info("the group size :{}", WsServerHandler.clientGroup.size());
        logger.info("the  clients size:{}", clients.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("wsServerchanle: {}", cause);
    }
}
