import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Start {

    public static void main(String[] args) throws InterruptedException {

        /**
         *
         *  bossgroup listen connection use epoll(linux) when the epoll_wait() method back
         *  and the socket accept() method back
         *  then the connected channel run in the workgroup listenning the read and write request
         *
         *
         *   socket = listener.accept() --->bossgroup
         *
         *   socket.read() or socket.write()---->workgroup
         *
         *
         */
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)//the tcp half connecting queue size
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//enalbe tcp keepalive
                    .childOption(ChannelOption.TCP_NODELAY, true) //enable Nagle algorithm
                    .childHandler(new ServersChannelInitializer());
            Channel ch = bootstrap.bind(8080).sync().channel();
            ch.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
