package info.chaintech.rpc.java.nioserver.http;

import info.chaintech.rpc.java.nioserver.IMessageReader;
import info.chaintech.rpc.java.nioserver.Message;
import info.chaintech.rpc.java.nioserver.MessageBuffer;
import info.chaintech.rpc.java.nioserver.Socket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
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
        // this.nextMessage.setMetaData(new HttpHeaders());

    }

    @Override
    public void read(Socket socket, ByteBuffer byteBuffer) throws IOException {
        int bytesRead = socket.read(byteBuffer);
        byteBuffer.flip();

        if (byteBuffer.remaining() == 0) {
            byteBuffer.clear();
            return;
        }

        nextMessage.writeToMessage(byteBuffer);

        // todo

        byte[] sharedArray = nextMessage.getSharedArray();
        byte[] dest = new byte[nextMessage.getLength()];
        System.arraycopy(sharedArray, nextMessage.getOffset(), dest, 0, nextMessage.getLength());
        log.info(new String(dest));

        byteBuffer.clear();
    }

    @Override
    public List<Message> getMessages() {
        return completeMessages;
    }
}
