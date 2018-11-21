package info.chaintech.rpc.java.nioserver;

/**
 * Start a nio server
 *
 * Created by Administrator on 2018/11/21 0021.
 *
 * Ref:
 *   - http://tutorials.jenkov.com/java-nio/non-blocking-server.html
 *   - http://tutorials.jenkov.com/java-performance/resizable-array.html
 */
public class ExampleMain {

    public static void main(String[] args) {
        Server server = new Server(9999, null, null);
        server.start();
    }
}
