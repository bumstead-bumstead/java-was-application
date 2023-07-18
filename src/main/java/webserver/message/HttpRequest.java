package webserver.message;


import com.google.common.io.ByteStreams;

import static utils.HttpMessageParser.*;
import static webserver.RequestHandler.TEMPLATES_PATH;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private URI uri;
    private String version;
    private Map<String, String> headers;

    public HttpRequest(HttpMethod method, URI uri, String version, Map<String, String> metadata) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = metadata;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URI getURI() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

//    public static HttpRequest createHttpRequestHeaderWithBufferedReader(BufferedReader bufferedReader) throws IOException {
//        List<String> requestHeaderList = parseBufferedReader(bufferedReader);
//        List<String> requestLine = parseRequestLine(requestHeaderList.remove(0));
//
//        HttpMethod method = HttpMethod.valueOf(requestLine.get(0));
//        String URI = requestLine.get(1);
//        String version = requestLine.get(2);
//
//        Map<String, String> metadata = parseMetadata(requestHeaderList);
//
//        return new HttpRequest(method, URI, version, metadata);
//    }

    //역할 분리
    public byte[] getBytesOfGetRequest() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEMPLATES_PATH + this.uri.getPath());

        return ByteStreams.toByteArray(fileInputStream);
    }
}
