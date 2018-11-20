package info.chaintech.rpc.java.network;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Java Network Stream
 * Created by Administrator on 2018/11/20 0020.
 */

@Slf4j
public class NetworkStream {


    public static void main(String[] args) {
        log.info("For test the java network stream");

        justTestSomething();
    }

    public static void justTestSomething() {
        // The byte to be written is the eight low-order bits of the argument b.
        // The 24 high-order bits of b are ignored.
        byte b = (byte) 1000;   // for the low eight bits, others are ignored
        byte bb = (byte) 100;   // it's ok

        // --------------------------------------
        try {
            InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
            log.info(inetAddress.toString());

            // will throw UnknownHostException
            // InetAddress inetAddress2 = InetAddress.getByName("com");

            InetAddress inetAddressDomian = InetAddress.getByName("1.2.3.4");
            log.info(inetAddressDomian.toString());

            InetAddress[] inetAddressAll = InetAddress.getAllByName("www.baidu.com");
            Arrays.asList(inetAddressAll).forEach(inet -> log.info(inet.toString()));

            InetAddress localhost = InetAddress.getLocalHost();
            log.info(localhost.toString());
        } catch (UnknownHostException e) {
            log.error("", e);
        }
    }
}
