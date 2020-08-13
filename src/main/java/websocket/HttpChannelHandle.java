package websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class HttpChannelHandle extends SimpleChannelInboundHandler<FullHttpRequest> {
    ObjectMapper mapper = new ObjectMapper();
    //    public static final ConcurrentHashMap<String, ChannelEntity> hashmap = new ConcurrentHashMap();

    // the key  is channel url and value is the clients in  this channel
    public static final List<String> users = new ArrayList<>();

    private BiPredicate<HttpMethod, HttpMethod> matchMethod = (method, target) -> method.equals(target);
    private BiPredicate<HttpHeaders, String> matchType = (headers, type) -> headers.get("Content-Type").contains(type);


    static {
        users.add("/ws/bb");
        users.add("/ws/bb/cc");
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        if (matchMethod.test(msg.method(), HttpMethod.GET)) {

        }

        /*if (matchMethod.test(msg.method(), HttpMethod.POST) && matchType.test(msg.headers(), "application/json")) {
            String s = msg.content().toString();
            ChannelEntity entity = mapper.readValue(s, ChannelEntity.class);
            URL url = new URL(msg.uri());
            if (hashmap.contains(url.getPath())) {
                hashmap.put(url.getPath(), entity);
                ByteBuf byteBuf = Unpooled.copiedBuffer("success".getBytes());
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
                ctx.writeAndFlush(response);
            } else {
                ByteBuf byteBuf = Unpooled.copiedBuffer("the channel existed".getBytes());
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

                ctx.writeAndFlush(response);
            }

        } else {
            ByteBuf byteBuf = Unpooled.copiedBuffer("not support".getBytes());
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            ctx.writeAndFlush(response);
        }*/
    }


}
