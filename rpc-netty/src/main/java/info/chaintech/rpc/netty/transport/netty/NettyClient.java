package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.InFlightRequests;
import info.chaintech.rpc.netty.transport.Transport;
import info.chaintech.rpc.netty.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class NettyClient implements TransportClient {
    private Bootstrap bootstrap;
    private EventLoopGroup ioLoopGroup;
    private final InFlightRequests inFlightRequests;
    private List<Channel> channels = new LinkedList<>();

    public NettyClient() {
        inFlightRequests = new InFlightRequests();
    }

    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException {
        return new NettyTransport(createChannel(address, connectionTimeout), inFlightRequests);
    }

    private Channel createChannel(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null!");
        }
        if (ioLoopGroup == null) {
            ioLoopGroup = newIoLoopGroup();
        }

        if (bootstrap == null) {
            ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandlerPipeline, ioLoopGroup);
        }

        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException();
        }

        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);

        return channel;
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup ioLoopGroup) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .group(ioLoopGroup)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseDecoder())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }

    private EventLoopGroup newIoLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        }
        return new NioEventLoopGroup();
    }

    @Override
    public void close() {
        for (Channel channel : channels) {
            if (channel != null) {
                channel.close();
            }
        }
        if (ioLoopGroup != null) {
            ioLoopGroup.shutdownGracefully();
        }
        inFlightRequests.close();
    }
}
