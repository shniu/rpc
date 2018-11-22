package info.chaintech.rpc.java.nioserver;

import java.util.Queue;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class WriteProxy {
    private Message message;

    public WriteProxy(MessageBuffer writeMessageBuffer, Queue<Message> outboundMessageQueue) {

    }

    public Message getMessage() {
        return message;
    }

    public void enqueue(Message respMessage) {

    }
}
