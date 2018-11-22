package info.chaintech.rpc.java.nioserver.http;

import info.chaintech.rpc.java.nioserver.IMessageReader;
import info.chaintech.rpc.java.nioserver.IMessageReaderFactory;

/**
 * Created by Administrator on 2018/11/22 0022.
 */
public class HttpMessageReaderFactory implements IMessageReaderFactory {


    @Override
    public IMessageReader createMessageReader() {
        return new HttpMessageReader();
    }
}
