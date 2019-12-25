package info.chaintech.rpc.netty.transport;

import info.chaintech.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 网络传输
 */
public interface Transport {
    /**
     * 发送请求
     *
     * @param request 请求
     * @return 结果
     */
    CompletableFuture<Command> send(Command request);
}
