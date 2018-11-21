package info.chaintech.rpc.java.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;

/**
 * Socket acceptor
 * Created by Administrator on 2018/11/21 0021.
 */
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

        }
    }
}
