package info.chaintech.rpc.netty;

import info.chaintech.rpc.api.RpcAccessPoint;
import info.chaintech.rpc.api.spi.ServiceSupport;
import info.chaintech.rpc.netty.client.StubFactory;
import info.chaintech.rpc.netty.server.ServiceProviderRegistry;
import info.chaintech.rpc.netty.transport.RequestHandlerRegistry;
import info.chaintech.rpc.netty.transport.Transport;
import info.chaintech.rpc.netty.transport.TransportClient;
import info.chaintech.rpc.netty.transport.TransportServer;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);

    private TransportServer server = null;
    private final ServiceProviderRegistry serviceProviderRegistry =
            ServiceSupport.load(ServiceProviderRegistry.class);

    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private StubFactory stubFactory = ServiceSupport.load(StubFactory.class);


    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
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
