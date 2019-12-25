package info.chaintech.rpc.sample.java.nioserver.http;

/**
 * Http header
 * Created by Administrator on 2018/11/24 0024.
 */
public class HttpHeaders {

    public static int HTTP_METHOD_GET    = 1;
    public static int HTTP_METHOD_POST   = 2;
    public static int HTTP_METHOD_PUT    = 3;
    public static int HTTP_METHOD_HEAD   = 4;
    public static int HTTP_METHOD_DELETE = 5;

    private int httpMethod    = 0;

    private int hostStartIndex = 0;
    private int hostEndIndex   = 0;

    private int contentLength = 0;

    private int bodyStartIndex = 0;
    private int bodyEndIndex   = 0;

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
    }

    public int getHostStartIndex() {
        return hostStartIndex;
    }

    public void setHostStartIndex(int hostStartIndex) {
        this.hostStartIndex = hostStartIndex;
    }

    public int getHostEndIndex() {
        return hostEndIndex;
    }

    public void setHostEndIndex(int hostEndIndex) {
        this.hostEndIndex = hostEndIndex;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getBodyStartIndex() {
        return bodyStartIndex;
    }

    public void setBodyStartIndex(int bodyStartIndex) {
        this.bodyStartIndex = bodyStartIndex;
    }

    public int getBodyEndIndex() {
        return bodyEndIndex;
    }

    public void setBodyEndIndex(int bodyEndIndex) {
        this.bodyEndIndex = bodyEndIndex;
    }

    @Override
    public String toString() {
        return "HttpHeaders{" +
                "httpMethod=" + httpMethod +
                ", hostStartIndex=" + hostStartIndex +
                ", hostEndIndex=" + hostEndIndex +
                ", contentLength=" + contentLength +
                ", bodyStartIndex=" + bodyStartIndex +
                ", bodyEndIndex=" + bodyEndIndex +
                '}';
    }
}
