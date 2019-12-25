package info.chaintech.rpc.server;

import info.chaintech.rpc.hello.HelloService;
import info.chaintech.rpc.server.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端程序
 */
@Slf4j
public class Server {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        helloService.hello("test");
        // Todo
        log.info("启动服务端程序");
    }
}
