package info.chaintech.rpc.netty.transport.netty;

import info.chaintech.rpc.netty.transport.Transport;
import info.chaintech.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

public class NettyTransport implements Transport {

    @Override
    public CompletableFuture<Command> send(Command request) {
        // Todo
        return null;
    }
}
