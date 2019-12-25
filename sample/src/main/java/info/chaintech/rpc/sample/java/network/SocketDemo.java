package info.chaintech.rpc.sample.java.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Socket demo
 * <p>
 * Created by Administrator on 2018/11/20 0020.
 */

@Slf4j
public class SocketDemo {


    public static void main(String[] args) {
        simpleSocketDemo();
    }

    private static void simpleSocketDemo() {
        try (Socket socket = new Socket("time.nist.gov", 13)) {

            socket.setSoTimeout(1000000);
            socket.setTcpNoDelay(true);
            socket.setSoLinger(true, 10);
            socket.setKeepAlive(true);


            InputStream is = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is, "ASCII");

            StringBuilder sb = new StringBuilder();
            for (int c = inputStreamReader.read(); c != -1; c = inputStreamReader.read()) {
                sb.append((char)c);
            }
            log.info(sb.toString());


        } catch (IOException e) {
            log.error("", e);
        }
    }
}
