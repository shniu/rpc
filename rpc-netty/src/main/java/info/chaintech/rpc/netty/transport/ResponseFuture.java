package info.chaintech.rpc.netty.transport;

import info.chaintech.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

public class ResponseFuture {
    private final int requestId;
    private final CompletableFuture<Command> completableFuture;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> completableFuture) {
        this.requestId = requestId;
        this.completableFuture = completableFuture;
        this.timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getCompletableFuture() {
        return completableFuture;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
