package info.chaintech.rpc.java.nioserver;

import java.util.Queue;

/**
 * Socket processor
 * Created by Administrator on 2018/11/21 0021.
 */
public class SocketProcessor implements Runnable {



    public SocketProcessor(Queue socketQueue,
                           MessageBuffer readBuffer,
                           MessageBuffer writeBuffer,
                           IMessageReaderFactory messageReaderFactory,
                           IMessageProcessor messageProcessor) {

    }

    @Override
    public void run() {

    }
}
