package info.chaintech.rpc.sample.java.nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public interface IMessageReader {
    void init(MessageBuffer readMessageBuffer);

    void read(Socket socket, ByteBuffer readByteBuffer) throws IOException;

    List<Message> getMessages();
}
