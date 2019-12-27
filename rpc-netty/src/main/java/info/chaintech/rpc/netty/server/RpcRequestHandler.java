package info.chaintech.rpc.netty.server;

import info.chaintech.rpc.netty.transport.RequestHandler;
import info.chaintech.rpc.netty.transport.command.Command;

public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
    @Override
    public <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {

    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public Command handle(Command requestCommand) {
        return null;
    }
}
