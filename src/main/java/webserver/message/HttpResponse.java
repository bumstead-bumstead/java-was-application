package webserver.message;

import java.util.Map;

/*
* response 객체 만들고,
* */
public class HttpResponse {
    private String version;
    private StatusCode statusCode;

    private Map<String, String> metadata;
    private String body;

    public HttpResponse(String version, StatusCode statusCode, Map<String, String> metadata, String body) {
        this.version = version;
        this.statusCode = statusCode;
        this.metadata = metadata;
        this.body = body;
    }

    //todo : default metadata 로직

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
