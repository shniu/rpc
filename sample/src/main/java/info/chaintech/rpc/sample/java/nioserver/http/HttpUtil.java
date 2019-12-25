package info.chaintech.rpc.sample.java.nioserver.http;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * Http utils
 * Created by Administrator on 2018/11/24 0024.
 */

@Slf4j
public class HttpUtil {

    private static final byte[] GET = new byte[]{'G', 'E', 'T'};
    private static final byte[] POST = new byte[]{'P', 'O', 'S', 'T'};
    private static final byte[] PUT = new byte[]{'P', 'U', 'T'};
    private static final byte[] HEAD = new byte[]{'H', 'E', 'A', 'D'};
    private static final byte[] DELETE = new byte[]{'D', 'E', 'L', 'E', 'T', 'E'};

    private static final byte[] HOST = new byte[]{'H', 'o', 's', 't'};
    private static final byte[] CONTENT_LENGTH = new byte[]{'C', 'o', 'n', 't', 'e', 'n', 't', '-', 'L', 'e', 'n', 'g', 't', 'h'};


    public static int parseHttpRequest(byte[] src, int startIndex, int endIndex, HttpHeaders httpHeaders) {

        // parse http request line
        int endOfFirstLine = findNextLineBreak(src, startIndex, endIndex);
        if (endOfFirstLine == -1) {
            return -1;
        }

        // parse http header
        int prevEndOfHeader = endOfFirstLine + 1;
        int endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);

        // Traverses all http headers to get the start index of the request body
        while (endOfHeader != -1 && endOfHeader != prevEndOfHeader + 1) {

            if (matches(src, prevEndOfHeader, CONTENT_LENGTH)) {
                try {
                    findContentLength(src, prevEndOfHeader, endIndex, httpHeaders);
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
            }

            prevEndOfHeader = endOfHeader + 1;
            endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);

        }

        if (endOfHeader == -1) {
            return -1;
        }

        // Check the request body message
        int bodyStartIndex = endOfHeader + 1;
        int bodyEndOfIndex = bodyStartIndex + httpHeaders.getContentLength();

        if (bodyEndOfIndex <= endIndex) {
            httpHeaders.setBodyStartIndex(bodyStartIndex);
            httpHeaders.setBodyEndIndex(bodyEndOfIndex);
            return bodyEndOfIndex;
        }

        return -1;
    }

    private static void findContentLength(byte[] src, int startIndex, int endIndex, HttpHeaders httpHeaders) throws UnsupportedEncodingException {
        int indexOfColon = findNext(src, startIndex, endIndex, (byte) ':');

        if (indexOfColon == -1) {
            throw new RuntimeException("Http header: Content-Length is not valid");
        }

        int index = indexOfColon + 1;
        while (src[index] == ' ') {
            index++;
        }

        int valueStartIndex = index;
        int valueEndIndex = index;
        boolean endOfValueFound = false;

        // Todo What if there is something non-numeric in the Content-Length's value? Fault-tolerant considerations
        while (index < endIndex && !endOfValueFound) {
            switch (src[index]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    index++;
                    break;
                }

                default: {
                    endOfValueFound = true;
                    valueEndIndex = index;
                }
            }
        }

        String contentLength = new String(src, valueStartIndex, valueEndIndex - valueStartIndex, "UTF-8");
        httpHeaders.setContentLength(Integer.parseInt(contentLength));

    }

    private static int findNext(byte[] src, int startIndex, int endIndex, byte value) {

        for (int i = startIndex; i < endIndex; i++) {
            if (src[i] == value) {
                return i;
            }
        }

        return -1;
    }

    private static boolean matches(byte[] src, int offset, byte[] value) {

        for (int i = offset, n = 0; n < value.length; i++, n++) {
            if (src[i] != value[n]) {
                return false;
            }
        }

        return true;
    }

    private static int findNextLineBreak(byte[] src, int startIndex, int endIndex) {

        for (int index = startIndex; index < endIndex; index++) {
            if (src[index] == '\n') {
                if (src[index - 1] == '\r') {
                    return index;
                }
            }
        }

        return -1;
    }
}
