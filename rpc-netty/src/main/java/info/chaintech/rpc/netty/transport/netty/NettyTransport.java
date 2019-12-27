package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.InFlightRequests;
import info.chaintech.rpc.netty.transport.ResponseFuture;
import info.chaintech.rpc.netty.transport.Transport;
import info.chaintech.rpc.netty.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

public class NettyTransport implements Transport {
    private Channel channel;
    private InFlightRequests inFlightRequests;

    public NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> future = new CompletableFuture<>();

        try {
            // 保存在途请求
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), future));

            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                // 处理发送失败
                if (!channelFuture.isSuccess()) {
                    future.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable throwable) {
            future.completeExceptionally(throwable);
        }

        return future;
    }
}
