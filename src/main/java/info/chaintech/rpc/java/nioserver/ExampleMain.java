package info.chaintech.rpc.java.nioserver;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * Start a nio server
 *
 * Created by Administrator on 2018/11/21 0021.
 *
 * Ref:
 *   - http://tutorials.jenkov.com/java-nio/non-blocking-server.html
 *   - http://tutorials.jenkov.com/java-performance/resizable-array.html
 */

@Slf4j
public class ExampleMain {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html>";

        byte[] httpResponseBytes = httpResponse.getBytes("UTF-8");

        IMessageProcessor messageProcessor = new IMessageProcessor() {
            @Override
            public void process(Message reqMessage, WriteProxy writeProxy) {
                log.info("Message Received from socket: ", reqMessage.getSocketId());

                Message respMessage = writeProxy.getMessage();
                respMessage.setSocketId(reqMessage.getSocketId());
                respMessage.writeToMessage(httpResponseBytes);

                writeProxy.enqueue(respMessage);
            }
        };

        Server server = new Server(9999, null, messageProcessor);
        server.start();
    }
}
