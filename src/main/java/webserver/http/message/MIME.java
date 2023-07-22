package webserver.http.message;

public enum MIME {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    TXT("text/plain");

    public String contentType;

    MIME(String contentType) {
        this.contentType = contentType;
    }
}
