package webserver.message;


import com.google.common.io.ByteStreams;

import static utils.ParsingUtils.*;
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
    private Map<String, String> metadata;

    public HttpRequest(HttpMethod method, String uri, String version, Map<String, String> metadata) {
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

    public static HttpRequest createHttpRequestHeaderWithBufferedReader(BufferedReader bufferedReader) throws IOException {
        List<String> requestHeaderList = parseBufferedReader(bufferedReader);
        List<String> requestLine = parseStringToList(requestHeaderList.remove(0));

        HttpMethod method = HttpMethod.valueOf(requestLine.get(0));
        String URI = requestLine.get(1);
        String version = requestLine.get(2);

        Map<String, String> metadata = parseListToMap(requestHeaderList);

        return new HttpRequest(method, URI, version, metadata);
    }

    public byte[] getBytesOfGetRequest() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEMPLATES_PATH + this.uri.getPath());

        return ByteStreams.toByteArray(fileInputStream);
    }
}
