package info.chaintech.rpc.sample.java.nioserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * Socket acceptor
 * Created by Administrator on 2018/11/21 0021.
 */
@Slf4j
public class SocketAcceptor implements Runnable {

    private int tcpPort;
    private Queue socketQueue;

    private ServerSocketChannel serverSocketChannel = null;

    public SocketAcceptor(int tcpPort, Queue socketQueue) {
        this.tcpPort = tcpPort;
        this.socketQueue = socketQueue;
    }

    @Override
    public void run() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(tcpPort));
            log.info("Start the SocketAcceptor successfully, and ready to accept the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                log.info("Socket accepted: {}", socketChannel);

                // todo check the queue
                //noinspection unchecked
                socketQueue.add(new Socket(socketChannel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
