package info.chaintech.rpc.netty.transport;

public interface TransportServer {
    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();
}
