package info.chaintech.rpc.netty.server;

import info.chaintech.rpc.netty.client.ServiceTypes;
import info.chaintech.rpc.netty.client.stubs.RpcRequest;
import info.chaintech.rpc.netty.serialize.SerializeSupport;
import info.chaintech.rpc.netty.transport.RequestHandler;
import info.chaintech.rpc.netty.transport.command.Code;
import info.chaintech.rpc.netty.transport.command.Command;
import info.chaintech.rpc.netty.transport.command.Header;
import info.chaintech.rpc.netty.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
    private static final Logger log = LoggerFactory.getLogger(RpcRequestHandler.class);

    private Map<String, Object> serviceProviders = new HashMap<>();

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);

        log.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();

        // 反序列化出 RpcRequest 对象
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());

        try {
            // 找接口的服务提供者
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serviceProvider, arg);
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()),
                        SerializeSupport.serialize(result));
            }

            // 如果没找到，返回NO_PROVIDER错误响应。
            log.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(),
                    Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            log.warn("Exception: ", t);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(),
                    Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
        }
    }
}
