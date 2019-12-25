package info.chaintech.rpc.api;

import info.chaintech.rpc.api.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

/**
 * RPC 框架对外提供的服务接口
 */
public interface RpcAccessPoint extends Closeable {

    /**
     * 客户端获取远程服务的引用
     *
     * @param uri          远程服务地址
     * @param serviceClass 服务类接口的 Class
     * @param <T>          服务接口的类型
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    /**
     * 服务端注册远程服务的实现实例
     *
     * @param service      实现实例
     * @param serviceClass 服务类接口的 Class
     * @param <T>          服务接口的类型
     * @return
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 获取注册中心的引用
     *
     * @param nameServiceUri 注册中心 URI
     * @return 注册中心引用
     */
    default NamingService getNameService(URI nameServiceUri) {
        Collection<NamingService> namingServices = ServiceSupport.loadAll(NamingService.class);
        for (NamingService namingService : namingServices) {
            if(namingService.supportedSchemes().contains(nameServiceUri.getScheme())) {
                namingService.connect(nameServiceUri);
                return namingService;
            }
        }

        return null;
    }

    /**
     * 服务端启动 RPC 服务，监听端口，开始提供远程服务
     *
     * @return 服务实例，可在需要关闭的时候关闭
     * @throws Exception 异常
     */
    Closeable startServer() throws Exception;
}
