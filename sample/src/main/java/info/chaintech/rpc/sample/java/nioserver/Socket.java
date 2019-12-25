package info.chaintech.rpc.sample.java.nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Custom  socket
 * Created by shniu on 2018/11/22.
 */
public class Socket {

    private long socketId;
    private SocketChannel socketChannel;
    private IMessageReader messageReader;
    private MessageWriter messageWriter;

    private boolean endOfStreamReached = false;

    public Socket() {

    }

    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {

        int read = socketChannel.read(byteBuffer);
        int totalRead = read;

        while (read > 0) {
            read = socketChannel.read(byteBuffer);
            totalRead += read;
        }

        if (read == -1) {
            endOfStreamReached = true;
        }

        return totalRead;

    }

    public int write(ByteBuffer byteBuffer) throws IOException {
        int written = socketChannel.write(byteBuffer);
        int totalWritten = written;

        while (written > 0 && byteBuffer.hasRemaining()) {
            written = socketChannel.write(byteBuffer);
            totalWritten += written;
        }

        return totalWritten;
    }

    public long getSocketId() {
        return socketId;
    }

    public void setSocketId(long socketId) {
        this.socketId = socketId;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public boolean isEndOfStreamReached() {
        return endOfStreamReached;
    }

    public void setEndOfStreamReached(boolean endOfStreamReached) {
        this.endOfStreamReached = endOfStreamReached;
    }

    public IMessageReader getMessageReader() {
        return messageReader;
    }

    public void setMessageReader(IMessageReader messageReader) {
        this.messageReader = messageReader;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void setMessageWriter(MessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    @Override
    public String toString() {
        return "Socket{" +
                "socketId=" + socketId +
                ", messageReader=" + messageReader +
                ", messageWriter=" + messageWriter +
                ", endOfStreamReached=" + endOfStreamReached +
                '}';
    }
}
