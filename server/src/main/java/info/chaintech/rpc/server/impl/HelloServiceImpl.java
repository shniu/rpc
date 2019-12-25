package info.chaintech.rpc.server.impl;

import info.chaintech.rpc.hello.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        log.info("HelloServiceImpl 收到：{}", name);
        String resp = "Hello, " + name;
        log.info("HelloServiceImpl 应答：{}", resp);
        return resp;
    }
}
