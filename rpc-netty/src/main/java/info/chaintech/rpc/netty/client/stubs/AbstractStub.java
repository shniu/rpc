package info.chaintech.rpc.netty.client.stubs;

import info.chaintech.rpc.netty.client.RequestIdSupport;
import info.chaintech.rpc.netty.client.ServiceStub;
import info.chaintech.rpc.netty.client.ServiceTypes;
import info.chaintech.rpc.netty.serialize.SerializeSupport;
import info.chaintech.rpc.netty.transport.Transport;
import info.chaintech.rpc.netty.transport.command.Code;
import info.chaintech.rpc.netty.transport.command.Command;
import info.chaintech.rpc.netty.transport.command.Header;
import info.chaintech.rpc.netty.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

public abstract class AbstractStub implements ServiceStub {
    protected Transport transport;

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected byte[] invokeRemote(RpcRequest request) {

        Header header = new Header(RequestIdSupport.next(), 1, ServiceTypes.TYPE_RPC_REQUEST);
        byte[] payload = SerializeSupport.serialize(request);
        Command requestCommand = new Command(header, payload);

        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
