package webserver.http.message;

import java.util.Arrays;
import java.util.Objects;

public class HttpResponse {
    private final String version;
    private final StatusCode statusCode;

    private final HttpMessageHeader headers;

    private final byte[] body;

    public static class Builder {
        private String version;
        private StatusCode statusCode;
        private HttpMessageHeader headers;
        private byte[] body;

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder statusCode(StatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder headers(HttpMessageHeader headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            if (version == null) {
                version = HttpHeaderUtils.DEFAULT_HTTP_VERSION;
            }
            if (headers == null) {
                headers = new HttpMessageHeader();
            }
            if (body == null) {
                body = new byte[]{};
            }
            if (statusCode == null) {
                statusCode = StatusCode.OK;
            }

            return new HttpResponse(version, statusCode, headers, body);
        }
    }

    private HttpResponse(String version, StatusCode statusCode, HttpMessageHeader headers, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
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

    public HttpMessageHeader getHeaders() {
        return headers;
    }

    public static HttpResponse generateRedirect(String path) {
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addLocation(path)
                .build();

        return new HttpResponse.Builder()
                .statusCode(StatusCode.FOUND)
                .headers(httpMessageHeader)
                .build();
    }

    public static HttpResponse generateError(StatusCode statusCode) {
        return new HttpResponse.Builder()
                .statusCode(statusCode)
                .build();
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
