package webserver;


import static utils.ParsingUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpRequestHeader {
    private HttpMethod method;
    private URI uri;
    private String version;
    private Map<String, String> metadata;

    public HttpRequestHeader(HttpMethod method, String uri, String version, Map<String, String> metadata) {
        this.method = method;
        this.uri = URI.createURIWithString(uri);
        this.version = version;
        this.metadata = metadata;
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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public static HttpRequestHeader createHttpRequestHeaderWithBufferedReader(BufferedReader bufferedReader) throws IOException {
        List<String> requestHeaderList = parseBufferedReader(bufferedReader);
        List<String> requestLine = parseStringToList(requestHeaderList.remove(0));

        HttpMethod method = HttpMethod.valueOf(requestLine.get(0));
        String URI = requestLine.get(1);
        String version = requestLine.get(2);

        Map<String, String> metadata = parseListToMap(requestHeaderList);

        return new HttpRequestHeader(method, URI, version, metadata);
    }
}
