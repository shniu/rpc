package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.RequestHandlerRegistry;
import info.chaintech.rpc.netty.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer implements TransportServer {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private int port;
    private RequestHandlerRegistry requestHandlerRegistry;
    private EventLoopGroup acceptEventGroup;
    private EventLoopGroup ioEventGroup;
    private Channel channel;

    @Override
    public void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception {
        this.port = port;
        this.requestHandlerRegistry = requestHandlerRegistry;
        EventLoopGroup acceptLoopGroup = newEventLoopGroup();
        EventLoopGroup ioLoopGroup = newEventLoopGroup();
        ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
        ServerBootstrap serverBootstrap = newBootstrap(channelHandlerPipeline, acceptLoopGroup, ioLoopGroup);

        Channel channel = doBind(serverBootstrap);

        this.acceptEventGroup = acceptLoopGroup;
        this.ioEventGroup = ioLoopGroup;
        this.channel = channel;
    }

    private Channel doBind(ServerBootstrap serverBootstrap) throws Exception {
        return serverBootstrap.bind(port)
                .sync()
                .channel();
    }

    private ServerBootstrap newBootstrap(ChannelHandler channelHandler,
                                         EventLoopGroup acceptLoopGroup,
                                         EventLoopGroup ioLoopGroup) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .group(acceptLoopGroup, ioLoopGroup)
                .childHandler(channelHandler)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return serverBootstrap;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new RequestDecoder())
                        .addLast(new ResponseEncoder())
                        .addLast(new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    private EventLoopGroup newEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    @Override
    public void stop() {
        if (acceptEventGroup != null) {
            acceptEventGroup.shutdownGracefully();
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }
}
