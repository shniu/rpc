package info.chaintech.rpc.java.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Non blocking client
 * Created by Administrator on 2018/11/21 0021.
 */
@Slf4j
public class NonBlockingClient {

    public static void main(String[] args) {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress("localhost", 7777);
            SocketChannel client = SocketChannel.open(socketAddress);

            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);

            while (client.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
