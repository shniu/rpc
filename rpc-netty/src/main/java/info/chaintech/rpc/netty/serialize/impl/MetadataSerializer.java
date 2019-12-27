package info.chaintech.rpc.netty.serialize.impl;

import info.chaintech.rpc.netty.nameservice.Metadata;
import info.chaintech.rpc.netty.serialize.Serializer;

/**
 * Metadata 序列化实现
 */
public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return 0;
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {

    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        return null;
    }

    @Override
    public byte type() {
        return 0;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return null;
    }
}
