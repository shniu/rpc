package info.chaintech.rpc.netty.serialize.impl;

import info.chaintech.rpc.netty.serialize.Serializer;

public class StringSerializer implements Serializer<String> {

    @Override
    public int size(String entry) {
        return 0;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {

    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return null;
    }

    @Override
    public byte type() {
        return 0;
    }

    @Override
    public Class<String> getSerializeClass() {
        return null;
    }
}
