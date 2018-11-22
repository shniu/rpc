package info.chaintech.rpc.java.nioserver;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class MessageWriter {
    private boolean empty;

    public boolean isEmpty() {
        return empty;
    }

    public void enqueue(Message outMessage) {

    }

    public void write(Socket socket, ByteBuffer writeByteBuffer) {


    }
}
