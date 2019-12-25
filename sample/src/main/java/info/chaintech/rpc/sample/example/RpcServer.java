package info.chaintech.rpc.sample.example;

import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by shniu on 2018/11/18.
 *
 * Ref:
 *   - https://stackoverflow.com/questions/5489915/java-datainputstream-read-operations-throwing-exceptions
 */

@Slf4j
public class RpcServer {

    private static int port = 8000;

    public static void main(String[] args) {
        log.info("Start server on {} ...", port);

        RpcServer rpcServer = new RpcServer();
        rpcServer.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                log.info("Server wait to accept a connection");
                Socket client = serverSocket.accept();
                new ServerHandlerThread(client);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private class ServerHandlerThread implements Runnable {

        private Socket socket;
        ServerHandlerThread(Socket socket) {
            this.socket = socket;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                // read data
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                String clientInputString = inputStream.readUTF();
                log.info("The client send content: {}", clientInputString);

                // write to client
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF("hello");

                // close
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        socket = null;
                        log.warn("Socket exception close: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
