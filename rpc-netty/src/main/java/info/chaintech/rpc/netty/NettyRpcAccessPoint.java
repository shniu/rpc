package info.chaintech.rpc.netty;

import info.chaintech.rpc.api.RpcAccessPoint;
import info.chaintech.rpc.api.spi.ServiceSupport;
import info.chaintech.rpc.netty.client.StubFactory;
import info.chaintech.rpc.netty.transport.RequestHandlerRegistry;
import info.chaintech.rpc.netty.transport.TransportClient;
import info.chaintech.rpc.netty.transport.TransportServer;

import java.io.Closeable;
import java.net.URI;

public class NettyRpcAccessPoint implements RpcAccessPoint {
    private TransportServer server;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private StubFactory stubFactory = ServiceSupport.load(StubFactory.class);

    private final int port = 9999;

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        return null;
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        return null;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (server == null) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }

        return () -> {
            if (server != null) {
                server.stop();
            }
        };
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop();
        }
        client.close();
    }
}
