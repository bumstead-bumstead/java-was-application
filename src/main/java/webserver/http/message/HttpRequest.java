package webserver.http.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final URI uri;
    private final String version;
    private final Map<String, String> headers;

    public class Builder {
        private HttpMethod httpMethod;
        private URI uri;
        private String version;
        private Map<String, String> headers;

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

        public Builder headers(Map<String, String> headers) {
            this.headers = new HashMap<>(headers);
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(httpMethod, uri, version, headers);
        }
    }

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
