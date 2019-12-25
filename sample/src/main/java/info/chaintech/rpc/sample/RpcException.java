package info.chaintech.rpc.sample;

/**
 * Created by shniu on 2018/11/18.
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RpcException() {
        super();
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

}
