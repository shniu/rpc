package info.chaintech.rpc.java.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Echo server
 * <p>
 * Created by Administrator on 2018/11/20 0020.
 */

@Slf4j
public class EchoServer {

    private final static int PORT = 7777;

    public static void main(String[] args) {

        ServerSocketChannel serverSocketChannel;
        Selector selector;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetAddress = new InetSocketAddress(PORT);
            serverSocket.bind(inetAddress);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("", e);
            return;
        }

        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                log.error("", e);
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                        SocketChannel client = server.accept();
                        log.info("Accept connection from {}", client);

                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                        clientKey.attach(byteBuffer);

                    }

                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        client.read(buffer);
                    }

                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        buffer.flip();
                        client.write(buffer);
                        buffer.compact();
                    }

                } catch (IOException e) {
                    log.error("", e);

                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        log.error("", e1);
                    }
                }
            }
        }


    }
}
