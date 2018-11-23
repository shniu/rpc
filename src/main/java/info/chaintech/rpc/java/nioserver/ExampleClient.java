package info.chaintech.rpc.java.nioserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Administrator on 2018/11/23 0023.
 */

@Slf4j
public class ExampleClient {

    public static void main(String[] args) {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 9999));
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer = ByteBuffer.wrap("Hello _".getBytes());
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            String resp = new String(buffer.array()).trim();
            log.info("resp: ", resp);
            buffer.clear();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
