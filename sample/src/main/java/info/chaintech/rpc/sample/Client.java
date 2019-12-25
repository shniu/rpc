package info.chaintech.rpc.sample;

import info.chaintech.rpc.sample.utils.ModelMapper;
import info.chaintech.rpc.sample.utils.Struct;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shniu on 2018/11/18.
 */

@Slf4j
public class Client {

    private static ModelMapper modelMapper = new ModelMapper();

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8080);
            for (int i = 0; i < 10; i++) {
                sendRpc(socket, "ping", "hello rpc");
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.info("Interrupted");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                }
            }
        }
    }

    private static void sendRpc(Socket socket, String messageType, String params) {
        // 请求消息体
        String reqParam = buildReqParam(messageType, params);

        // 请求长度前缀
        Struct struct = new Struct();
        byte[] prefixLength;
        try {
           prefixLength = struct.pack("I", reqParam.length());
        } catch (Exception e) {
            throw new RpcException("Pack req param error", e);
        }

        // send
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.write(prefixLength);
            outputStream.flush();
            outputStream.writeChars(reqParam);
            // outputStream.write(reqParam.getBytes("UTF-8"));
            outputStream.flush();

            // recv
            byte[] responseLength = new byte[4];
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            inputStream.read(responseLength);
            long[] unpack = struct.unpack("I", responseLength);
            int respBodyLength = (int) unpack[0];

            /*byte[] respBodyBytes = new byte[respBodyLength];
            inputStream.read(respBodyBytes);*/
            char[] respBodyChars = new char[respBodyLength];
            for (int i = 0; i < respBodyLength; i++) {
                respBodyChars[i] = inputStream.readChar();
            }
            log.info("Rpc response: {}", new String(respBodyChars));
        } catch (IOException e) {
            throw new RpcException(e);
        } catch (Exception e) {
            throw new RpcException("Unpack error", e);
        }
    }

    private static String buildReqParam(String messageType, String params) {

        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("in", messageType);
        reqParamMap.put("params", params);

        return modelMapper.writeString(reqParamMap);
    }
}
