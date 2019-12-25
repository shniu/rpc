package info.chaintech.rpc.sample.java.nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class MessageWriter {

    private List<Message> writeQueue = new ArrayList<>();
    private Message messageInProgress = null;
    private int bytesWritten = 0;

    public boolean isEmpty() {
        return writeQueue.isEmpty() && messageInProgress == null;
    }

    public void enqueue(Message outMessage) {
        if (messageInProgress == null) {
            messageInProgress = outMessage;
        } else {
            writeQueue.add(outMessage);
        }
    }

    public void write(Socket socket, ByteBuffer byteBuffer) throws IOException {

        byteBuffer.put(messageInProgress.getSharedArray(), messageInProgress.getOffset() + bytesWritten, messageInProgress.getLength() - bytesWritten);
        byteBuffer.flip();

        bytesWritten += socket.write(byteBuffer);
        byteBuffer.clear();

        if (bytesWritten >= messageInProgress.getLength()) {
            if (writeQueue.size() > 0) {
                messageInProgress = writeQueue.remove(0);
            } else {
                messageInProgress = null;
            }
        }

    }
}
