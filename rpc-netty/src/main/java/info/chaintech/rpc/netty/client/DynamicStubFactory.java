package info.chaintech.rpc.netty.client;

import com.itranswarp.compiler.JavaStringCompiler;
import info.chaintech.rpc.netty.transport.Transport;

import java.util.Map;

public class DynamicStubFactory implements StubFactory {

    private final static String STUB_SOURCE_TEMPLATE =
            "package info.chaintech.rpc.netty.client.stubs;\n" +
                    "import info.chaintech.rpc.netty.serialize.SerializeSupport;\n" +
                    "\n" +
                    "public class %s extends AbstractStub implements %s {\n" +
                    "    @Override\n" +
                    "    public String %s(String arg) {\n" +
                    "        return SerializeSupport.parse(\n" +
                    "                invokeRemote(\n" +
                    "                        new RpcRequest(\n" +
                    "                                \"%s\",\n" +
                    "                                \"%s\",\n" +
                    "                                SerializeSupport.serialize(arg)\n" +
                    "                        )\n" +
                    "                )\n" +
                    "        );\n" +
                    "    }\n" +
                    "}";

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "info.chaintech.rpc.netty.client.stubs." + stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();

            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName,
                    classFullName, methodName, classFullName, methodName);

            // 编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);

            // 加载编译好的类
            Class<?> aClass = compiler.loadClass(stubFullName, results);

            ServiceStub serviceStub = (ServiceStub) aClass.newInstance();
            serviceStub.setTransport(transport);
            //noinspection unchecked
            return (T) serviceStub;
        } catch (Throwable t) {
            throw new RuntimeException();
        }
    }
}
