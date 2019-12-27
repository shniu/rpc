package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.RequestHandler;
import info.chaintech.rpc.netty.transport.RequestHandlerRegistry;
import info.chaintech.rpc.netty.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger log = LoggerFactory.getLogger(RequestInvocation.class);

    private final RequestHandlerRegistry requestHandlerRegistry;

    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command request) throws Exception {
        RequestHandler handler = requestHandlerRegistry.get(request.getHeader().getType());
        if (handler != null) {
            Command response = handler.handle(request);
            if (response != null) {
                ctx.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        log.warn("Write response failed!", channelFuture.cause());
                        ctx.channel().close();
                    }
                });
            } else {
                log.warn("Response is null.");
            }
        } else {
            throw new Exception(String.format("No handler for request with type: %d!",
                    request.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Exception: ", cause);

        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) ctx.close();
    }
}
