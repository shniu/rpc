package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.command.Header;
import info.chaintech.rpc.netty.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class ResponseDecoder extends CommandDecoder {
    @Override
    protected Header decodeHeader(ChannelHandlerContext ctx, ByteBuf in) {
        int type = in.readInt();
        int version = in.readInt();
        int requestId = in.readInt();
        int code = in.readInt();
        int errorLength = in.readInt();
        byte[] errorBytes = new byte[errorLength];
        in.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);

        return new ResponseHeader(type, version, requestId, code, error);
    }
}
