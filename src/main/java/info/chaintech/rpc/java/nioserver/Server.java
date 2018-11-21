package info.chaintech.rpc.java.nioserver;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Nio server
 *
 * Created by Administrator on 2018/11/21 0021.
 */
public class Server {

    private SocketAcceptor socketAcceptor;
    private SocketProcessor socketProcessor;

    private int tcpPort = 0;
    private IMessageReaderFactory messageReaderFactory = null;
    private IMessageProcessor     messageProcessor = null;

    public Server(int tcpPort, IMessageReaderFactory messageReaderFactory, IMessageProcessor messageProcessor) {
        this.tcpPort = tcpPort;
        this.messageProcessor = messageProcessor;
        this.messageReaderFactory = messageReaderFactory;
    }

    /**
     * the entry to start a server
     */
    public void start() {
        Queue socketQueue = new ArrayBlockingQueue(1024);

        socketAcceptor = new SocketAcceptor(tcpPort, socketQueue);

        MessageBuffer writeBuffer = new MessageBuffer();
        MessageBuffer readBuffer = new MessageBuffer();
        socketProcessor = new SocketProcessor(socketQueue, readBuffer, writeBuffer, messageReaderFactory, messageProcessor);

        Thread acceptorThread = new Thread(socketAcceptor);
        Thread processorThread = new Thread(socketProcessor);
        acceptorThread.start();
        processorThread.start();
    }
}
