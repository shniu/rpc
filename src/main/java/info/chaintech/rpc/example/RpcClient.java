package info.chaintech.rpc.example;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * Created by shniu on 2018/11/18.
 */

@Slf4j
public class RpcClient {


    public static void main(String[] args) {

        while (true) {
            // send data
            send();

            // then sleep 1s
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }
    }

    private static void send() {
        Socket socket = null;

        try {
            socket = new Socket("localhost", 8000);
            socket.setSoTimeout(10000);

            // keyboard input
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            // write data to server
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(input.readLine());

            // read data from server
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String response = inputStream.readUTF();
            log.info("Server response: {}", response);

            // close
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    log.warn("Client socket close exception {}", e.getMessage());
                }
            }
        }
    }
}
