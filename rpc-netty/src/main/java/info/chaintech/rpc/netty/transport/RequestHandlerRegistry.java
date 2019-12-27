package info.chaintech.rpc.netty.transport;

import info.chaintech.rpc.api.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandlerRegistry {
    private static final Logger log = LoggerFactory.getLogger(RequestHandlerRegistry.class);

    private Map<Integer, RequestHandler> handlerMap = new ConcurrentHashMap<>();
    private static RequestHandlerRegistry instance = null;

    public static RequestHandlerRegistry getInstance() {
        if (instance == null) {
            instance = new RequestHandlerRegistry();
        }
        return instance;
    }

    private RequestHandlerRegistry() {
        Collection<RequestHandler> handlers = ServiceSupport.loadAll(RequestHandler.class);
        for (RequestHandler handler : handlers) {
            handlerMap.put(handler.type(), handler);
            log.info("Load request handler, type: {}, class: {}.",
                    handler.type(), handler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }
}
