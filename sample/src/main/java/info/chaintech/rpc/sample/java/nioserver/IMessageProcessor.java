package info.chaintech.rpc.sample.java.nioserver;

/**
 * Created by Administrator on 2018/11/21 0021.
 */
public interface IMessageProcessor {
    void process(Message message, WriteProxy writeProxy);
}
