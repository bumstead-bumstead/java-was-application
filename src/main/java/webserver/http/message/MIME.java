package webserver.http.message;

public enum MIME {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    TXT("text/plain"),
    WOFF("font/WOFF"),
    TTF("font/ttf");

    public String contentType;

    MIME(String contentType) {
        this.contentType = contentType;
    }

    public boolean isTemplate() {
        return this.equals(MIME.HTML) || this.equals(MIME.TXT);
    }
}
