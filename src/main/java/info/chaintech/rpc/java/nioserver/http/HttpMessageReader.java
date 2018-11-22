package info.chaintech.rpc.java.nioserver.http;

import info.chaintech.rpc.java.nioserver.IMessageReader;
import info.chaintech.rpc.java.nioserver.Message;
import info.chaintech.rpc.java.nioserver.MessageBuffer;
import info.chaintech.rpc.java.nioserver.Socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class HttpMessageReader implements IMessageReader {

    private MessageBuffer messageBuffer;
    private Message nextMessage = null;

    private List<Message> completeMessages = new ArrayList<>();

    @Override
    public void init(MessageBuffer readMessageBuffer) {

        this.messageBuffer = readMessageBuffer;
        this.nextMessage = messageBuffer.getMessage();
        // this.nextMessage.setMetaData(new HttpHeaders());

    }

    @Override
    public void read(Socket socket, ByteBuffer readByteBuffer) throws IOException {
        int bytesRead = socket.read(readByteBuffer);
        readByteBuffer.flip();

        if (readByteBuffer.remaining() == 0) {
            readByteBuffer.clear();
            return;
        }

        nextMessage.writeToMessage(readByteBuffer);
    }

    @Override
    public List<Message> getMessages() {
        return completeMessages;
    }
}
