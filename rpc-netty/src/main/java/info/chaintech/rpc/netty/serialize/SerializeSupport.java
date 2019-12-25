package info.chaintech.rpc.netty.serialize;

import info.chaintech.rpc.api.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用序列化
 */
public class SerializeSupport {
    private static final Logger log = LoggerFactory.getLogger(SerializeSupport.class);

    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();
    private static Map<Byte, Class<?>> typeMap = new HashMap<>();

    static {
        for (Serializer serializer : ServiceSupport.loadAll(Serializer.class)) {
            registerType(serializer.type(), serializer.getSerializeClass(), serializer);

            log.info("Found serializer, class: {}, type: {}.",
                    serializer.getSerializeClass().getCanonicalName(),
                    serializer.type());
        }
    }

    private static void registerType(byte type, Class serializeClass, Serializer serializer) {
        serializerMap.put(serializeClass, serializer);
        typeMap.put(type, serializeClass);
    }

    /**
     * 从字节数组反序列化
     *
     * @param buffer 字节数组
     * @param <E>    序列化类
     * @return 序列化对象
     */
    public static <E> E parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    private static <E> E parse(byte[] buffer, int offset, int length) {
        byte type = parseEntryType(buffer);
        //noinspection unchecked
        Class<E> eClass = (Class<E>) typeMap.get(type);
        if (eClass == null) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffer, offset + 1, length - 1, eClass);
        }
    }

    private static byte parseEntryType(byte[] buffer) {
        return buffer[0];
    }

    /**
     * 从字节数组反序列化
     *
     * @param buffer 字节数组
     * @param offset 位移
     * @param length 长度
     * @param eClass 序列化类 Class
     * @param <E>    序列化类
     * @return 序列化对象
     */
    public static <E> E parse(byte[] buffer, int offset, int length, Class<E> eClass) {
        Object entry = serializerMap.get(eClass).parse(buffer, offset, length);
        if (eClass.isAssignableFrom(entry.getClass())) {
            //noinspection unchecked
            return (E) entry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    /**
     * 序列化成字节数组
     *
     * @param entry 待序列化对象
     * @param <E>   序列化类
     * @return 字节数组
     */
    public static <E> byte[] serialize(E entry) {
        //noinspection unchecked
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }

        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }
}
