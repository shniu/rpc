package info.chaintech.rpc.netty.serialize.impl;

import info.chaintech.rpc.netty.nameservice.Metadata;
import info.chaintech.rpc.netty.serialize.Serializer;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Metadata 序列化实现
 */
public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return Short.BYTES +
                entry.entrySet().stream()
                        .mapToInt(this::entrySize)
                        .sum();
    }

    private int entrySize(Map.Entry<String, List<URI>> entry) {
        return Short.BYTES +
                entry.getKey().getBytes(StandardCharsets.UTF_8).length +
                Short.BYTES +
                entry.getValue().stream()
                        .mapToInt(uri -> Short.BYTES +
                                uri.toASCIIString().getBytes(StandardCharsets.UTF_8).length)
                        .sum();
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putShort(toShortSafely(entry.size()));

        entry.forEach((k, v) -> {
            byte[] kBytes = k.getBytes(StandardCharsets.UTF_8);
            buffer.putShort(toShortSafely(kBytes.length));
            buffer.put(kBytes);

            buffer.putShort(toShortSafely(v.size()));
            for (URI uri : v) {
                byte[] uriBytes = uri.toASCIIString().getBytes(StandardCharsets.UTF_8);
                buffer.putShort(toShortSafely(uriBytes.length));
                buffer.put(uriBytes);
            }
        });
    }

    private short toShortSafely(int v) {
        assert v < Short.MAX_VALUE;
        return (short) v;
    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);

        Metadata metadata = new Metadata();
        short sizeOfMetadata = buffer.getShort();
        for (int i = 0; i < sizeOfMetadata; i++) {
            int keyLen = buffer.getShort();
            byte[] keyBytes = new byte[keyLen];
            buffer.get(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);

            short uriListSize = buffer.getShort();
            List<URI> uriList = new ArrayList<>(uriListSize);
            for (int j = 0; j < uriListSize; j++) {
                short uriLen = buffer.getShort();
                byte[] uriBytes = new byte[uriLen];
                buffer.get(uriBytes);
                URI uri = URI.create(new String(uriBytes, StandardCharsets.UTF_8));
                uriList.add(uri);
            }

            metadata.put(key, uriList);
        }

        return metadata;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }
}
