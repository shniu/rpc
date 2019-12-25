package info.chaintech.rpc.sample.java.nioserver;

/**
 * QueueIntFlip
 * <p>
 * Created by Administrator on 2018/11/23 0023.
 */
public class QueueIntFlip {

    private int[] elements;

    private int capacity = 0;
    private int writePos = 0;
    private int readPos = 0;
    private boolean flipped = false;

    public QueueIntFlip(int capacity) {
        this.capacity = capacity;
        this.elements = new int[this.capacity];
    }

    public void reset() {
        this.writePos = 0;
        this.readPos = 0;
        this.flipped = false;
    }

    public int available() {
        if (flipped) {
            return capacity - readPos + writePos;
        } else {
            return writePos - readPos;
        }
    }

    public int remainingCapacity() {
        if (flipped) {
            return readPos - writePos;
        }

        return capacity - writePos;
    }

    public boolean put(int element) {

        if (!flipped) {
            if (writePos == capacity) {
                writePos = 0;
                flipped = true;

                if (writePos >= readPos) {
                    return false;
                }
            }

        } else {
            if (writePos >= readPos) {
                return false;
            }
        }

        elements[writePos++] = element;
        return true;
    }

    public int take() {
        if (!flipped) {
            if (readPos < writePos) {
                return elements[readPos++];
            } else {
                return -1;
            }
        } else {
            if (readPos == capacity) {
                readPos = 0;
                flipped = false;

                if (readPos < writePos) {
                    return elements[readPos++];
                } else {
                    return -1;
                }
            } else {
                return elements[readPos++];
            }
        }
    }

    public int[] getElements() {
        return elements;
    }

    public void setElements(int[] elements) {
        this.elements = elements;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getWritePos() {
        return writePos;
    }

    public void setWritePos(int writePos) {
        this.writePos = writePos;
    }

    public int getReadPos() {
        return readPos;
    }

    public void setReadPos(int readPos) {
        this.readPos = readPos;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    @Override
    public String toString() {
        return "QueueIntFlip{" +
                "capacity=" + capacity +
                ", writePos=" + writePos +
                ", readPos=" + readPos +
                ", flipped=" + flipped +
                '}';
    }
}
