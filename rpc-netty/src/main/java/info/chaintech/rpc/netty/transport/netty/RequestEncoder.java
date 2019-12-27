package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RequestEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(ctx, header, byteBuf);
    }
}
