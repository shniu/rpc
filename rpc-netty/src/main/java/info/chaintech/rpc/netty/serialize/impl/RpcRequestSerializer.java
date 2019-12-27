package info.chaintech.rpc.netty.serialize.impl;

import info.chaintech.rpc.netty.client.stubs.RpcRequest;
import info.chaintech.rpc.netty.serialize.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest request) {
        return Integer.BYTES + request.getInterfaceName().getBytes(StandardCharsets.UTF_8).length
                + Integer.BYTES + request.getMethodName().getBytes(StandardCharsets.UTF_8).length
                + Integer.BYTES + request.getSerializedArguments().length;
    }

    @Override
    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] tmpBytes = request.getInterfaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = request.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        tmpBytes = request.getSerializedArguments();
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmp = new byte[len];
        buffer.get(tmp);
        String interfaceName = new String(tmp, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmp = new byte[len];
        buffer.get(tmp);
        String methodName = new String(tmp, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmp = new byte[len];
        buffer.get(tmp);
        byte[] serializedArgs = tmp;

        return new RpcRequest(interfaceName, methodName, serializedArgs);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
