package info.chaintech.rpc.client;

import info.chaintech.rpc.api.NamingService;
import info.chaintech.rpc.api.RpcAccessPoint;
import info.chaintech.rpc.api.spi.ServiceSupport;
import info.chaintech.rpc.hello.HelloService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 客户端程序
 */
@Slf4j
public class Client {

    public static void main(String[] args) {

        String helloServiceName = HelloService.class.getCanonicalName();

        File tmpFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpFile, "simple_rpc_name_service.data");

        log.info("启动客户端程序");
        String name = "Master";

        // 加载 RPC 框架的访问点
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            // 找到注册中心
            NamingService namingService = rpcAccessPoint.getNameService(file.toURI());
            assert namingService != null;
            // 从注册中心查询服务提供者的地址
            URI uri = namingService.lookupService(helloServiceName);
            assert uri != null;
            log.info("找到服务 {} 的服务提供者 {}", helloServiceName, uri);

            // 获取到服务提供者的引用，实际上是一个 Stub 代理
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            log.info("请求服务, name: {}", name);
            // 执行服务调用，委托给了 Stub 代理
            String resp = helloService.hello(name);
            log.info("接受到请求：{}", resp);
        } catch (IOException e) {
            log.error("Error", e);
        }
    }
}
