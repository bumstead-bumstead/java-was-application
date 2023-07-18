package webserver.message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/*
* response 객체 만들고,
* */
public class HttpResponse {
    private String version;
    private StatusCode statusCode;

    private Map<String, String> headers;

    private byte[] body;
    private HttpResponse(String version, StatusCode statusCode, Map<String, String> metadata, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.headers = metadata;
        this.body = body;
    }

    private HttpResponse(StatusCode statusCode, byte[] body) {
        this.version = "HTTP/1.1";
        this.headers = Map.of("Content-Type", "text/html;charset=utf-8", "Content-Length", String.valueOf(body.length));
        this.statusCode = statusCode;
        this.body = body;
    }

    public static HttpResponse generateHttpResponse(StatusCode statusCode, byte[] body) {
        return new HttpResponse(statusCode, body);
    }

    public void writeResponse(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBytes(version + " " + statusCode.codeNumber + " " + statusCode.name() + "\r\n");
        for (Map.Entry<String, String> line : headers.entrySet()) {
            dataOutputStream.writeBytes(line.getKey() + ": " + line.getValue() + "\r\n");
        }
        dataOutputStream.writeBytes("Content-Length: " + body.length + "\r\n");
        dataOutputStream.writeBytes("\r\n");
    }

    public byte[] getBody() {
        return body;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
