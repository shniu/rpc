package info.chaintech.rpc.java.nioserver;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class Message {
    private long socketId;

    public void setSocketId(long socketId) {
        this.socketId = socketId;
    }

    public long getSocketId() {
        return socketId;
    }

    public void writeToMessage(byte[] httpResponseBytes) {

    }

    public void writeToMessage(ByteBuffer readByteBuffer) {

    }
}
