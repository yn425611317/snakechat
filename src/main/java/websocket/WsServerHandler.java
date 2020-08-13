package websocket;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class WsServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger
            = LoggerFactory.getLogger(IdleHandler.class);

    //the line of clients
    public static final ConcurrentHashMap<String, ChannelGroup> clientGroup = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, ChannelId> clients = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    // url:type:id@  type 1 single chat 2 group chat
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        try {
            int i = text.indexOf("@");
            String protocolHead = text.substring(0, i);
            String[] split = protocolHead.split(":");
            String channel = split[0];
            String type = split[1];

            if (clientGroup.containsKey(channel.trim())) {
                //single chat
                if (type.equals(ChatType.SINGLE.getType())) {
                    //group chat
                    Channel channel1 = clientGroup.get(channel).find(clients.get(split[2]));
                    channel1.writeAndFlush(new TextWebSocketFrame("server peer: " + text.substring(i + 1)));
                } else if (type.equals(ChatType.GROUP.getType())) {
                    clientGroup.get(channel).writeAndFlush(new TextWebSocketFrame("server: " + text.substring(i + 1)));

                }
            }

        } catch (Exception e) {

            ctx.writeAndFlush(new TextWebSocketFrame(e.getMessage()));
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
