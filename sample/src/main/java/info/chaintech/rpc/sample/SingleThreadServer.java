package info.chaintech.rpc.sample;

import info.chaintech.rpc.sample.utils.ModelMapper;
import info.chaintech.rpc.sample.utils.Struct;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shniu on 2018/11/18.
 */
@Slf4j
public class SingleThreadServer {

    private static Struct struct = new Struct();
    private static ModelMapper modelMapper = new ModelMapper();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            // serverSocket.setSoTimeout(10000);
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(8080));
            // Socket client = serverSocket.accept();
            loop(serverSocket);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void loop(ServerSocket serverSocket) throws Exception {
        while (true) {
            Socket client = serverSocket.accept();
            handleConn(client);
        }
    }

    private static void handleConn(Socket client) throws Exception {
        log.info("From {}", client.getRemoteSocketAddress());

        while (true) {

            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            byte[] prefix = new byte[4];
            inputStream.read(prefix);
            long[] unpack = struct.unpack("I", prefix);
            log.info(String.valueOf(unpack[0]));
            if (unpack[0] <= 0) {
                log.info("From {} connection, bye bye", client.getRemoteSocketAddress());
                client.close();
                break;
            }

            int bodyLength = (int) unpack[0];
            // read req body
            byte[] reqBodyBytes = new byte[bodyLength];

            // inputStream.read(reqBodyBytes);
            char[] body = new char[bodyLength];
            for (int i = 0; i < unpack[0]; i++) {
                body[i] = inputStream.readChar();
            }
            //inputStream.readUTF();
            log.info("Req body: {}", new String(body));
            @SuppressWarnings("unchecked")
            Map<String, Object> reqBodyMap = (Map<String, Object>) modelMapper.make(HashMap.class, new String(body));
            if (reqBodyMap.getOrDefault("in", "").equals("ping")) {
                ping(client, reqBodyMap);
            }
        }

    }

    private static void ping(Socket client, Map<String, Object> reqBodyMap) throws IOException {
        // output
        DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        Map<String, Object> respMap = new HashMap<>(16);
        respMap.put("out", "pong");
        respMap.put("result", reqBodyMap.get("params"));
        String respResult = modelMapper.writeString(respMap);

        byte[] respResultPrefix;
        try {
            respResultPrefix = struct.pack("I", respResult.length());
        } catch (Exception e) {
            throw new RpcException(e);
        }

        outputStream.write(respResultPrefix);
        outputStream.flush();
        outputStream.writeChars(respResult);
        outputStream.flush();
    }
}
