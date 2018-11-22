package info.chaintech.rpc.java.nioserver;

import java.nio.ByteBuffer;

/**
 * Message of inbound or outbound
 * <p>
 * Created by Administrator on 2018/11/22 0022.
 */
public class Message {

    /**
     * Message payload
     */
    private MessageBuffer messageBuffer;

    private long socketId;

    public Object metaData;

    public Message(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public int writeToMessage(byte[] httpResponseBytes) {
        return 0;
    }

    public int writeToMessage(ByteBuffer byteBuffer) {

        return 0;
    }

    public void setSocketId(long socketId) {
        this.socketId = socketId;
    }

    public long getSocketId() {
        return socketId;
    }

    public MessageBuffer getMessageBuffer() {
        return messageBuffer;
    }

    public void setMessageBuffer(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public Object getMetaData() {
        return metaData;
    }

    public void setMetaData(Object metaData) {
        this.metaData = metaData;
    }
}
