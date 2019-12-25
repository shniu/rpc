package info.chaintech.rpc.netty.serialize.impl.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.chaintech.rpc.netty.serialize.Serializer;

public abstract class JsonSerializer<T> implements Serializer<T> {
    protected static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // 设置 ObjectMapper 的一些属性
    }

    @Override
    public int size(T entry) {
        try {
            return Integer.BYTES + objectMapper.writeValueAsBytes(entry).length;
        } catch (JsonProcessingException e) {
            return 0;
        }
    }
}
