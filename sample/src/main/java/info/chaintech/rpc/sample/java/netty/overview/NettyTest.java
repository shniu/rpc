package info.chaintech.rpc.sample.java.netty.overview;

import io.netty.buffer.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Netty test
 * Created by Administrator on 2018/11/26 0026.
 */
public class NettyTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testNioChannel() {

    }

    @Test
    public void testByteBuf() {
        UnpooledHeapByteBuf heapByteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 10, 100);
        if (heapByteBuf.hasArray()) {
            byte[] array = heapByteBuf.array();
            int offset = heapByteBuf.arrayOffset() + heapByteBuf.readerIndex();
            int length = heapByteBuf.readableBytes();
        }

        UnpooledByteBufAllocator unpooledByteBufAllocator = new UnpooledByteBufAllocator(true);
        ByteBuf directBuffer = unpooledByteBufAllocator.directBuffer();

        PooledByteBufAllocator pooledByteBufAllocator;
    }

}