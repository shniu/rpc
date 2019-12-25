package info.chaintech.rpc.sample.java.nioserver;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for QueueIntFlip
 * Created by Administrator on 2018/11/23 0023.
 */

@Slf4j
public class QueueIntFlipTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test0() {
        int smallCapacity = 32;
        int arrayLength = 16 * smallCapacity;
        QueueIntFlip queueIntFlip = new QueueIntFlip(16);
        for (int i = 0; i < arrayLength; i += smallCapacity) {
            queueIntFlip.put(i);
        }

        log.info("{}", queueIntFlip.toString());
        log.info("{}", queueIntFlip.available());
        log.info("{}", queueIntFlip.remainingCapacity());
    }

}