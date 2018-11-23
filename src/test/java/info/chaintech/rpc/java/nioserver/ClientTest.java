package info.chaintech.rpc.java.nioserver;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by Administrator on 2018/11/23 0023.
 */
public class ClientTest {

    @Test
    public void test() throws IOException, InterruptedException {

        InetSocketAddress serverAddr = new InetSocketAddress("localhost", 9999);
        SocketChannel client = SocketChannel.open(serverAddr);

        byte[] message = "Google".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(message);
        client.write(byteBuffer);
        byteBuffer.clear();

        Thread.sleep(100000);

    }

    public void a() throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);

        SelectionKey key1 = socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

}
