package info.chaintech.rpc.sample.java.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    final SocketChannel socketChannel;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel = channel;
        channel.configureBlocking(false);
        sk = socketChannel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == READING) read();
            else if (state == SENDING) send();
        } catch (IOException e) {

        }
    }

    private void send() throws IOException {
        socketChannel.write(output);
        if (outputIsComplete()) sk.cancel();
    }

    private boolean outputIsComplete() {
        return false;
    }

    private void read() throws IOException {
        socketChannel.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void process() {

    }

    private boolean inputIsComplete() {
        return false;
    }
}
