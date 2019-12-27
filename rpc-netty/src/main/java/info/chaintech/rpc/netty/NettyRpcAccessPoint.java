package info.chaintech.rpc.netty;

import info.chaintech.rpc.api.RpcAccessPoint;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public class NettyRpcAccessPoint implements RpcAccessPoint {
    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        return null;
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        return null;
    }

    @Override
    public Closeable startServer() throws Exception {

        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
