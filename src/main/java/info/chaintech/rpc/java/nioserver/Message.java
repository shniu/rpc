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

    private int capacity;
    private byte[] sharedArray;
    private int offset;
    private int length;

    public Object metaData;

    public Message(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public int writeToMessage(byte[] byteArray) {
        return writeToMessage(byteArray, 0, byteArray.length);
    }

    public int writeToMessage(byte[] byteArray, int offset, int length) {
        //noinspection UnnecessaryLocalVariable
        int remaining = length;

        while (this.length + remaining > capacity) {
            if (!messageBuffer.expandMessage(this)) {
                return -1;
            }
        }

        int bytesToCopy = Math.min(remaining, this.capacity - this.length);
        System.arraycopy(byteArray, offset, this.sharedArray, this.offset + this.length, bytesToCopy);
        this.length += bytesToCopy;

        return bytesToCopy;
    }

    public int writeToMessage(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();

        while (length + remaining > capacity) {
            if (!messageBuffer.expandMessage(this)) {
                return -1;
            }
        }

        int bytesToCopy = Math.min(remaining, capacity - length);
        byteBuffer.get(sharedArray, offset + length, bytesToCopy);
        length += bytesToCopy;
        return bytesToCopy;
    }

    public void writePartialMessageToMessage(Message message, int endIndex) {
        int startIndexOfPartialMessage = message.offset + endIndex;
        int lengthOfPartialMessage = (message.offset + message.length) - endIndex;

        System.arraycopy(message.getSharedArray(), startIndexOfPartialMessage, sharedArray, offset, lengthOfPartialMessage);
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public byte[] getSharedArray() {
        return sharedArray;
    }

    public void setSharedArray(byte[] sharedArray) {
        this.sharedArray = sharedArray;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
