package webserver.message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final String version;
    private final StatusCode statusCode;

    private final Map<String, String> headers;

    private final byte[] body;

    private HttpResponse(String version, StatusCode statusCode, Map<String, String> metadata, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.headers = metadata;
        this.body = body;
    }

    public HttpResponse(StatusCode statusCode, byte[] body) {
        this.version = "HTTP/1.1";
        this.headers = Map.of("Content-Type", "text/html;charset=utf-8", "Content-Length", String.valueOf(body.length));
        this.statusCode = statusCode;
        this.body = body;
    }

    public static HttpResponse generateHttpResponse(StatusCode statusCode, byte[] body) {
        return new HttpResponse(statusCode, body);
    }

    public byte[] getBody() {
        return body;
    }

    public String getVersion() {
        return version;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return Objects.equals(version, that.version) && statusCode == that.statusCode && Objects.equals(headers, that.headers) && Arrays.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(version, statusCode, headers);
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }
}
