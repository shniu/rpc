package info.chaintech.rpc.netty.client;


import info.chaintech.rpc.netty.transport.Transport;

public interface ServiceStub {
    void setTransport(Transport transport);
}
