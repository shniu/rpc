package info.chaintech.rpc.sample.java.nioserver.http;

import info.chaintech.rpc.sample.java.nioserver.IMessageReader;
import info.chaintech.rpc.sample.java.nioserver.Message;
import info.chaintech.rpc.sample.java.nioserver.MessageBuffer;
import info.chaintech.rpc.sample.java.nioserver.Socket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Http message reader
 * Created by Administrator on 2018/11/22 0022.
 */

@Slf4j
public class HttpMessageReader implements IMessageReader {

    private MessageBuffer messageBuffer;
    private Message nextMessage = null;

    private List<Message> completeMessages = new ArrayList<>();

    @Override
    public void init(MessageBuffer readMessageBuffer) {

        this.messageBuffer = readMessageBuffer;
        this.nextMessage = messageBuffer.getMessage();
        this.nextMessage.setMetaData(new HttpHeaders());

    }

    /**
     * parse the message
     *
     * @param socket     socket
     * @param byteBuffer byteBuffer
     * @throws IOException exp
     */
    @Override
    public void read(Socket socket, ByteBuffer byteBuffer) throws IOException {
        int bytesRead = socket.read(byteBuffer);
        byteBuffer.flip();

        if (byteBuffer.remaining() == 0) {
            byteBuffer.clear();
            return;
        }

        nextMessage.writeToMessage(byteBuffer);

        int endIndex = HttpUtil.parseHttpRequest(nextMessage.getSharedArray(), nextMessage.getOffset(), nextMessage.getLength() + nextMessage.getOffset(), (HttpHeaders) nextMessage.getMetaData());

        // Just for debug
        byte[] sharedArray = nextMessage.getSharedArray();
        byte[] dest = new byte[nextMessage.getLength()];
        System.arraycopy(sharedArray, nextMessage.getOffset(), dest, 0, nextMessage.getLength());
        log.info(new String(dest));

        if (endIndex != -1) {
            Message message = messageBuffer.getMessage();
            message.setMetaData(new HttpHeaders());

            message.writePartialMessageToMessage(nextMessage, endIndex);
            completeMessages.add(nextMessage);
            nextMessage = message;
        }

        byteBuffer.clear();
    }

    @Override
    public List<Message> getMessages() {
        return completeMessages;
    }
}
