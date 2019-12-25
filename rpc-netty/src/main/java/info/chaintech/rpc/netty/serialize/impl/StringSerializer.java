package info.chaintech.rpc.netty.serialize.impl;

import info.chaintech.rpc.netty.serialize.Serializer;

import java.nio.charset.StandardCharsets;

public class StringSerializer implements Serializer<String> {

    @Override
    public int size(String entry) {
        return entry.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] entryBytes = entry.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(entryBytes, 0, bytes, offset, length);
    }

    @Override
    public String parse(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    @Override
    public byte type() {
        return Types.TYPE_STRING;
    }

    @Override
    public Class<String> getSerializeClass() {
        return String.class;
    }
}
