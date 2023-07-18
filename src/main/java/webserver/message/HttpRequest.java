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
    private final HttpMethod method;
    private final URI uri;
    private final String version;
    private final Map<String, String> headers;

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

    //역할 분리
    public byte[] getBytesOfGetRequest() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEMPLATES_PATH + this.uri.getPath());

        return ByteStreams.toByteArray(fileInputStream);
    }
}
