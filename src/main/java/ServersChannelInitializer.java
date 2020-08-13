import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import websocket.HttpChannelHandle;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import websocket.IdleHandler;
import websocket.WsServerHandler;

import java.util.concurrent.TimeUnit;

public class ServersChannelInitializer extends ChannelInitializer {
    protected void initChannel(Channel ch) {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(10,10,10, TimeUnit.SECONDS));
        pipeline.addLast(new IdleHandler());
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new WebSocketServerCompressionHandler())
                .addLast(new WebSocketServerProtocolHandler("/snake", null, true, 8192));
        pipeline.addLast(new WsServerHandler());
        pipeline.addLast(new HttpChannelHandle());

    }
}
