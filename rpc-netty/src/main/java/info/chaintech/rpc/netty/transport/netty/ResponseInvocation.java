package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.InFlightRequests;
import info.chaintech.rpc.netty.transport.ResponseFuture;
import info.chaintech.rpc.netty.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger log = LoggerFactory.getLogger(ResponseInvocation.class);

    private final InFlightRequests inFlightRequests;

    public ResponseInvocation(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command response) throws Exception {
        ResponseFuture future = inFlightRequests.remove(response.getHeader().getRequestId());
        if (future != null) {
            future.getCompletableFuture().complete(response);
        } else {
            log.warn("Drop response: {}", response);
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
