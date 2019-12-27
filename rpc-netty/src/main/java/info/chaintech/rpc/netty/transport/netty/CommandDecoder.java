package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.command.Command;
import info.chaintech.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public abstract class CommandDecoder extends ByteToMessageDecoder {
    private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable(LENGTH_FIELD_LENGTH)) {
            return;
        }

        in.markReaderIndex();
        int length = in.readInt() - LENGTH_FIELD_LENGTH;

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        Header header = decodeHeader(ctx, in);
        int payloadLength = length - header.length();
        byte[] payload = new byte[payloadLength];
        in.readBytes(payload);
        out.add(new Command(header, payload));
    }

    protected abstract Header decodeHeader(ChannelHandlerContext ctx, ByteBuf in);
}
