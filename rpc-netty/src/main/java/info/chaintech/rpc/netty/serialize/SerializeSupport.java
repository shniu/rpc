package info.chaintech.rpc.netty.serialize;

/**
 * 通用序列化
 */
public class SerializeSupport {

    /**
     * 从字节数组反序列化
     *
     * @param bytes 字节数组
     * @param <E>   序列化类
     * @return 序列化对象
     */
    public static <E> E parse(byte[] bytes) {
        return null;
    }

    /**
     * 序列化成字节数组
     *
     * @param entry 待序列化对象
     * @param <E>   序列化类
     * @return 字节数组
     */
    public static <E> byte[] serialize(E entry) {
        return null;
    }
}
