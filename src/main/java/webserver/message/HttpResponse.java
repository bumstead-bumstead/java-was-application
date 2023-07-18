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

    private Map<String, String> metadata;
    private byte[] body;

    private HttpResponse(String version, StatusCode statusCode, Map<String, String> metadata, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.metadata = metadata;
        this.body = body;
    }

    private HttpResponse(StatusCode statusCode, byte[] body) {
        this.version = "HTTP/1.1";
        this.metadata = Map.of("Content-Type", "text/html;charset=utf-8", "Content-Length", String.valueOf(body.length));
        this.statusCode = statusCode;
        this.body = body;
    }

    public static HttpResponse generateHttpResponse(StatusCode statusCode, byte[] body) {
        return new HttpResponse(statusCode, body);
    }

    public void writeResponse(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBytes(version + " " + statusCode.codeNumber + " " + statusCode.name() + "\r\n");
        for (Map.Entry<String, String> line : metadata.entrySet()) {
            dataOutputStream.writeBytes(line.getKey() + ": " + line.getValue() + "\r\n");
        }
        dataOutputStream.writeBytes("Content-Length: " + body.length + "\r\n");
        dataOutputStream.writeBytes("\r\n");
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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
