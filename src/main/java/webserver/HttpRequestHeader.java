package webserver;


import utils.HttpHeaderParsingUtils;

import static utils.HttpHeaderParsingUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private HttpMethod method;
    private String URI;
    private String version;
    private Map<String, String> metadata;

    public HttpRequestHeader(HttpMethod method, String URI, String version, Map<String, String> metadata) {
        this.method = method;
        this.URI = URI;
        this.version = version;
        this.metadata = metadata;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public static HttpRequestHeader createHttpRequestHeaderWithBufferedReader(BufferedReader bufferedReader) throws IOException {
        List<String> requestHeaderList = parseBufferedReader(bufferedReader);
        List<String> requestLine = parseRequestLine(requestHeaderList.remove(0));

        HttpMethod method = HttpMethod.valueOf(requestLine.get(0));
        String URI = requestLine.get(1);
        String version = requestLine.get(2);

        Map<String, String> metadata = parseMetaData(requestHeaderList);

        return new HttpRequestHeader(method, URI, version, metadata);
    }
}
