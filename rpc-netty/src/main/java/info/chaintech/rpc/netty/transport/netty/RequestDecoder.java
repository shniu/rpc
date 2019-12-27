package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RequestDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf in) {
        return new Header(in.readInt(), in.readInt(), in.readInt());
    }
}
