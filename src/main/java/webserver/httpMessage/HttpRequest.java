package webserver.httpMessage;


import com.google.common.io.ByteStreams;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static webserver.RequestHandler.TEMPLATES_PATH;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final URI uri;
    private final String version;
    private final Map<String, String> headers;

    public HttpRequest(HttpMethod method, URI uri, String version, Map<String, String> headers) {
        this.httpMethod = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return httpMethod == that.httpMethod && Objects.equals(uri, that.uri) && Objects.equals(version, that.version) && Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, uri, version, headers);
    }
}
