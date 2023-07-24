package webserver.http.message;

import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final URI uri;
    private final String version;
    private final HttpMessageHeader headers;
    private final Map<String, String> body;

    public static class Builder {

        private HttpMethod httpMethod;

        private URI uri;
        private String version;
        private HttpMessageHeader headers;
        private Map<String, String> body;
        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }
        public Builder URI(URI uri) {
            this.uri = uri;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder headers(HttpMessageHeader headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(Map<String, String> body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            if (body == null) {
                body = Map.of();
            }
            if (headers == null) {
                headers = new HttpMessageHeader();
            }

            return new HttpRequest(httpMethod, uri, version, headers, body);
        }
    }
    public HttpRequest(HttpMethod method, URI uri, String version, HttpMessageHeader headers, Map<String, String> body) {
        this.httpMethod = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }
    public boolean containsBody() {
        return !body.isEmpty();
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

    public HttpMessageHeader getHeaders() {
        return headers;
    }

    public Map<String, String> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return httpMethod == that.httpMethod && Objects.equals(uri, that.uri) && Objects.equals(version, that.version) && Objects.equals(headers, that.headers) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, uri, version, headers, body);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", uri=" + uri +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
