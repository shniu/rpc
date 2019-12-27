package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.command.Header;
import info.chaintech.rpc.netty.transport.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext ctx, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(ctx, header, byteBuf);

        if (header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            int errorLength = header.length() - (Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                    Integer.BYTES);
            byteBuf.writeInt(errorLength);
            byteBuf.writeBytes(responseHeader.getError() == null ?
                    new byte[0] : responseHeader.getError().getBytes(StandardCharsets.UTF_8));
        } else {
            throw new Exception(String.format("Invalid header type: %s!",
                    header.getClass().getCanonicalName()));
        }
    }
}
