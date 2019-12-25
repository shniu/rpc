package info.chaintech.rpc.sample.java.network;

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
 *
 * - https://www.baeldung.com/java-nio-selector
 * - http://tutorials.jenkov.com/java-nio/non-blocking-server.html
 */

@Slf4j
public class EchoServer {

    private final static int PORT = 7777;

    public static void main(String[] args) {

        ServerSocketChannel serverSocketChannel;
        Selector selector;

        try {
            // 创建一个 server socket 通道
            serverSocketChannel = ServerSocketChannel.open();

            // 得到一个 server socket
            ServerSocket serverSocket = serverSocketChannel.socket();

            // 绑定监听的地址
            InetSocketAddress inetAddress = new InetSocketAddress(PORT);
            serverSocket.bind(inetAddress);

            // 配置为非阻塞通道
            serverSocketChannel.configureBlocking(false);

            // 创建 selector
            selector = Selector.open();
            // 注册selector,并设置interestSet
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("", e);
            return;
        }

        while (true) {
            try {
                // also, selector.select(TIMEOUT) == 0
                int selectChannels = selector.select();
                log.info("selector.select() = {}", selectChannels);
            } catch (IOException e) {
                log.error("", e);
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            // 遍历所有ready的channel进行对应操作
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                // 获取到 selection key 之后就删除掉, 表示我们已经对这个 IO 事件进行了处理.
                iterator.remove();

                try {
                    // OP_ACCEPT
                    if (key.isAcceptable()) {
                        // 这里返回的是 ServerSocketChannel
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                        SocketChannel client = server.accept();
                        log.info("Accept connection from {}", client);

                        client.configureBlocking(false);
                        SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                        clientKey.attach(byteBuffer);

                    }

                    // OP_READ
                    if (key.isReadable()) {
                        // 这里返回的是 SocketChannel
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // key.interestOps();
                        client.read(buffer);
                    }

                    // OP_WRITE
                    if (key.isWritable()) {
                        // 这里返回的是 SocketChannel
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
