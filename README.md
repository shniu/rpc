# rpc

rpc framework.

## RPC 原理

RPC 框架的目标是让远程服务调用更加简单、透明；RPC 框架负责屏蔽底层的传输方式（TCP Or UDP）、序列化方式（XML/JSON/二进制）和通信细节。

![RPC](https://static001.geekbang.org/resource/image/b2/fb/b265dc0bd6eae1b88b236f517609c9fb.png)


可见，一个 RPC 框架的核心功能有：

1. 客户端端动态代理和Stub设计
2. 通用高效的序列化和反序列化
3. 高性能的网络传输
4. 高性能高并发的服务端设计（如负载均衡、异步网络服务等）
5. 高可用的服务注册和发现能力

### RPC 开源框架

#### 关于 Dubbo

#### 关于 gRPC

## 模块说明

```text
sample: tech spike 样例代码

client:  客户端例子
server:  服务端例子
rpc-api: rpc 框架的接口定义
rpc-netty: rpc 框架的实现
hello-service-impl: hello 服务的实现
```
