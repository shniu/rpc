package info.chaintech.rpc.netty.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 在途请求
 */
public class InFlightRequests implements Closeable {
    private final static long TIMEOUT_SEC = 10L;
    private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
    private final Semaphore semaphore = new Semaphore(10);

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if (semaphore.tryAcquire(TIMEOUT_SEC, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture responseFuture = futureMap.remove(requestId);
        if (responseFuture != null) {
            semaphore.release();
        }
        return responseFuture;
    }

    @Override
    public void close() {

    }
}
