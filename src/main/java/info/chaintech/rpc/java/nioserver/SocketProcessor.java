package info.chaintech.rpc.java.nioserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

/**
 * Socket processor
 * Created by Administrator on 2018/11/21 0021.
 */

@Slf4j
public class SocketProcessor implements Runnable {

    private Queue<Socket> inboundSocketQueue;
    private MessageBuffer readMessageBuffer;
    private MessageBuffer writeMessageBuffer;
    private IMessageReaderFactory messageReaderFactory;
    private IMessageProcessor messageProcessor;

    private Map<Long, Socket> socketMap = new HashMap<>();

    private long nextSocketId = 16 * 1024;

    private ByteBuffer readByteBuffer = ByteBuffer.allocate(1024 * 1024);
    private ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024 * 1024);

    // selector
    private Selector readSelector;
    private Selector writeSelector;
    private WriteProxy writeProxy;

    private Queue<Message> outboundMessageQueue = new LinkedList<>();  // todo opt

    private Set<Socket> emptyToNonEmptySockets = new HashSet<>();
    private Set<Socket> nonEmptyToEmptySockets = new HashSet<>();

    public SocketProcessor(Queue<Socket> inboundSocketQueue,
                           MessageBuffer readMessageBuffer,
                           MessageBuffer writeMessageBuffer,
                           IMessageReaderFactory messageReaderFactory,
                           IMessageProcessor messageProcessor) throws IOException {
        this.inboundSocketQueue = inboundSocketQueue;
        this.readMessageBuffer = readMessageBuffer;
        this.writeMessageBuffer = writeMessageBuffer;
        this.messageReaderFactory = messageReaderFactory;
        this.messageProcessor = messageProcessor;

        writeProxy = new WriteProxy(this.writeMessageBuffer, outboundMessageQueue);

        readSelector = Selector.open();
        writeSelector = Selector.open();
    }

    @Override
    public void run() {
        log.info("Start the SocketProcessor successfully, and ready to handle the connection");

        while (true) {
            try {
                executeCycle();
            } catch (IOException e) {
                log.error("", e);
                // break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("", e);
                // break;
            }
        }
    }

    private void executeCycle() throws IOException {
        takeNewSockets();
        readFromSockets();
        writeToSockets();
    }

    private void writeToSockets() throws IOException {
        takeNewOutboundMessages();

        cancelEmptySockets();

        registerNonEmptySockets();

        int writeReady = writeSelector.selectNow();
        if (writeReady > 0) {
            Set<SelectionKey> selectedKeys = writeSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                Socket socket = (Socket) key.attachment();
                socket.getMessageWriter().write(socket, writeByteBuffer);

                if (socket.getMessageWriter().isEmpty()) {
                    nonEmptyToEmptySockets.add(socket);
                }

                iterator.remove();
            }

            selectedKeys.clear();
        }
    }

    private void registerNonEmptySockets() throws ClosedChannelException {
        for(Socket socket : emptyToNonEmptySockets){
            socket.getSocketChannel().register(writeSelector, SelectionKey.OP_WRITE, socket);
        }
        emptyToNonEmptySockets.clear();
    }

    private void cancelEmptySockets() {
        for (Socket socket : nonEmptyToEmptySockets) {
            SelectionKey key = socket.getSocketChannel().keyFor(this.writeSelector);
            key.cancel();
        }
        nonEmptyToEmptySockets.clear();
    }

    private void takeNewOutboundMessages() {
        Message outMessage = outboundMessageQueue.poll();

        while (outMessage != null) {
            Socket socket = socketMap.get(outMessage.getSocketId());

            if (socket != null) {
                MessageWriter messageWriter = socket.getMessageWriter();
                if (messageWriter.isEmpty()) {
                    messageWriter.enqueue(outMessage);
                    nonEmptyToEmptySockets.remove(socket);
                    emptyToNonEmptySockets.add(socket);
                } else {
                    messageWriter.enqueue(outMessage);
                }
            }

            outMessage = outboundMessageQueue.poll();
        }
    }

    private void readFromSockets() throws IOException {
        // 立刻返回所有可读的 Channel
        int readReady = readSelector.selectNow();

        if (readReady > 0) {
            Set<SelectionKey> selectedKeys = readSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                readFromSocket(key);
                keyIterator.remove();
            }

            selectedKeys.clear();
        }
    }

    private void readFromSocket(SelectionKey key) throws IOException {
        Socket socket = (Socket) key.attachment();
        log.info("read from: {}", socket.getSocketId());
        socket.getMessageReader().read(socket, readByteBuffer);

        List<Message> fullMessages = socket.getMessageReader().getMessages();
        if (fullMessages.size() > 0) {
            for (Message message : fullMessages) {
                message.setSocketId(socket.getSocketId());
                messageProcessor.process(message, writeProxy);
            }

            fullMessages.clear();
        }

        if (socket.isEndOfStreamReached()) {
            log.info("Socket closed: {}", socket.getSocketId());
            socketMap.remove(socket.getSocketId());
            key.attach(null);
            key.cancel();
            key.channel().close();
        }
    }

    private void takeNewSockets() throws IOException {
        Socket newSocket = inboundSocketQueue.poll();

        while (newSocket != null) {
            newSocket.setSocketId(nextSocketId++);
            newSocket.getSocketChannel().configureBlocking(false);
            newSocket.setMessageReader(messageReaderFactory.createMessageReader());
            newSocket.getMessageReader().init(readMessageBuffer);
            newSocket.setMessageWriter(new MessageWriter());

            socketMap.put(newSocket.getSocketId(), newSocket);

            SelectionKey key = newSocket.getSocketChannel().register(readSelector, SelectionKey.OP_READ);
            key.attach(newSocket);

            log.info("Take a new socket: {}", newSocket);

            newSocket = inboundSocketQueue.poll();
        }
    }
}
