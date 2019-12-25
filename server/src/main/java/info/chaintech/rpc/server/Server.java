package info.chaintech.rpc.server;

import info.chaintech.rpc.api.NamingService;
import info.chaintech.rpc.api.RpcAccessPoint;
import info.chaintech.rpc.api.spi.ServiceSupport;
import info.chaintech.rpc.hello.HelloService;
import info.chaintech.rpc.server.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

/**
 * 服务端程序
 */
@Slf4j
public class Server {

    public static void main(String[] args) {
        log.info("启动服务端程序");

        HelloService helloService = new HelloServiceImpl();
        String helloServiceName = HelloService.class.getCanonicalName();

        File tmpFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpFile, "simple_rpc_name_service.data");

        // 加载 RPC 框架并启动 RPC 服务
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer()) {
            // 找到注册中心
            NamingService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;

            // 添加服务提供者
            log.info("向 RPC 框架注册服务 {}", helloServiceName);
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);

            // 在注册中心注册服务
            log.info("向注册中心注册服务 {}", helloServiceName);
            nameService.registerService(helloServiceName, uri);

            log.info("开始提供服务，任意键退出");
            System.in.read();
            log.info("Bye Bye!");
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
