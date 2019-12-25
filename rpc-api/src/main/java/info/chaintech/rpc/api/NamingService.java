package info.chaintech.rpc.api;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心接口
 */
public interface NamingService {

    /**
     * 连接注册中心
     *
     * @param nameServiceUri 注册中心地址
     */
    void connect(URI nameServiceUri);

    /**
     * 服务端注册服务
     *
     * @param serviceName 服务名称
     * @param uri         服务地址
     */
    void registerService(String serviceName, URI uri) throws IOException;

    /**
     * 客户端查询服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     * @throws IOException IO 异常
     */
    URI lookupService(String serviceName) throws IOException;

    /**
     * 所有支持的协议
     */
    Collection<String> supportedSchemes();
}
