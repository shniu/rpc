package info.chaintech.rpc.java.nioserver;

import java.util.Queue;

/**
 *
 * Created by Administrator on 2018/11/22 0022.
 */
public class WriteProxy {
    private MessageBuffer messageBuffer = null;
    private Queue writeQueue = null;


    public WriteProxy(MessageBuffer messageBuffer, Queue<Message> writeQueue) {
        this.messageBuffer = messageBuffer;
        this.writeQueue = writeQueue;
    }

    public Message getMessage() {
        return messageBuffer.getMessage();
    }

    @SuppressWarnings("unchecked")
    public boolean enqueue(Message message) {
        return writeQueue.offer(message);
    }
}
