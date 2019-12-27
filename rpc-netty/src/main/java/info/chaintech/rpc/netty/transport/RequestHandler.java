package info.chaintech.rpc.netty.transport;

import info.chaintech.rpc.netty.transport.command.Command;

public interface RequestHandler {
    int type();

    Command handle(Command requestCommand);
}
