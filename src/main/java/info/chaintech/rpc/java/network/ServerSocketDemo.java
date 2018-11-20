package info.chaintech.rpc.java.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server socket
 * <p>
 * Created by Administrator on 2018/11/20 0020.
 */
@Slf4j
public class ServerSocketDemo {

    private final static int PORT = 11111;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(50);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                log.info("Server startup, listen on {}", PORT);
                Socket clientConn = serverSocket.accept();
                DaytimeTask daytimeTask = new DaytimeTask(clientConn);
                pool.submit(daytimeTask);
            }

        } catch (IOException e) {
            log.error("", e);
        }

    }

    private static class DaytimeTask implements Callable<Void> {

        private Socket connection;

        DaytimeTask(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() throws Exception {

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            Date now = new Date();
            writer.write(now.toString() + " hello \r\n");
            writer.flush();
            return null;

        }
    }
}
