package info.chaintech.rpc.java.nioserver;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClientTest {

    @Test
    public void test() throws IOException, InterruptedException {

        InetSocketAddress serverAddr = new InetSocketAddress("localhost", 9999);
        SocketChannel client = SocketChannel.open(serverAddr);

        String requestMessage = "GET /hello HTTP/1.1\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 6\r\n\r\nGoogle";

        byte[] message = requestMessage.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.wrap(message);
        client.write(writeBuffer);
        writeBuffer.clear();

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        client.read(readBuffer);
        String response = new String(readBuffer.array()).trim();
        log.info("Response: {}", response);
        readBuffer.clear();

        client.close();
    }

    public void a() throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);

        SelectionKey key1 = socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    @Test
    public void testBinary() {
        int i = -1 & 0x3f;
        log.info(String.valueOf(i));

        log.info(Integer.toBinaryString(-1));
        log.info(Integer.toBinaryString(0x3f));
    }

}
