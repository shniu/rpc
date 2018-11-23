package info.chaintech.rpc.java.nioserver;

/**
 *
 * Created by Administrator on 2018/11/21 0021.
 */
public class MessageBuffer {

    private static final int KB = 1024;
    private static final int MB = 1024 * KB;

    private static final int CAPACITY_SMALL = 4 * KB;
    private static final int CAPACITY_MEDIUM = 128 * KB;
    private static final int CAPACITY_LARGE = MB;

    private byte[] smallMessageBuffer = new byte[1024 * CAPACITY_SMALL];
    private byte[] mediumMessageBuffer = new byte[128 * CAPACITY_MEDIUM];
    private byte[] largeMessageBuffer = new byte[16 * CAPACITY_LARGE];

    private QueueIntFlip smallMessageBufferFreeBlocks = new QueueIntFlip(1024);
    private QueueIntFlip mediumMessageBufferFreeBlocks = new QueueIntFlip(128);
    private QueueIntFlip largeMessageBufferFreeBlocks = new QueueIntFlip(16);


    public MessageBuffer() {
        for (int i = 0; i < smallMessageBuffer.length; i += CAPACITY_SMALL) {
            smallMessageBufferFreeBlocks.put(i);
        }
        for (int i = 0; i < mediumMessageBuffer.length; i += CAPACITY_MEDIUM) {
            mediumMessageBufferFreeBlocks.put(i);
        }
        for (int i = 0; i < largeMessageBuffer.length; i += CAPACITY_LARGE) {
            largeMessageBufferFreeBlocks.put(i);
        }
    }


    public Message getMessage() {

        int nextFreeSmallBlock = smallMessageBufferFreeBlocks.take();
        if (nextFreeSmallBlock == -1) {
            return null;
        }

        Message message = new Message(this);
        message.setCapacity(CAPACITY_SMALL);
        message.setOffset(nextFreeSmallBlock);
        message.setSharedArray(smallMessageBuffer);
        message.setLength(0);

        return message;
    }

    public boolean expandMessage(Message message) {

        if (message.getCapacity() == CAPACITY_SMALL) {
            return moveMessage(message, smallMessageBufferFreeBlocks, mediumMessageBufferFreeBlocks, mediumMessageBuffer, CAPACITY_MEDIUM);
        } else if (message.getCapacity() == CAPACITY_MEDIUM) {
            return moveMessage(message, mediumMessageBufferFreeBlocks, largeMessageBufferFreeBlocks, largeMessageBuffer, CAPACITY_LARGE);
        }

        return false;
    }

    private boolean moveMessage(Message message,
                                QueueIntFlip srcBlockQueue,
                                QueueIntFlip destBlockQueue,
                                byte[] destBuffer,
                                int newCapacity) {

        int nextFreeBlock = destBlockQueue.take();
        if (nextFreeBlock == -1) {
            return false;
        }

        System.arraycopy(message.getSharedArray(), message.getOffset(), destBuffer, nextFreeBlock, message.getLength());

        srcBlockQueue.put(message.getOffset());

        message.setSharedArray(destBuffer);
        message.setOffset(nextFreeBlock);
        message.setCapacity(newCapacity);

        return true;
    }
}
