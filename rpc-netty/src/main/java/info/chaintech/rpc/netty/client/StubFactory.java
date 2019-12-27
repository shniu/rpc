package info.chaintech.rpc.netty.client;

import info.chaintech.rpc.netty.transport.Transport;

public interface StubFactory {
    <T> T createStub(Transport transport, Class<T> serviceClass);
}
