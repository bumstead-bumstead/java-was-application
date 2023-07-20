package webserver.http.message;

public enum StatusCode {
    OK (200, "Ok"),
    CREATED (201, "Created"),
    ACCEPTED (202, "Accepted"),
    MULTIPLE_CHOICES (300, "Multiple Choiceㄴ"),
    MOVED_PERMANENTLY (301, "Moved_Permanently"),
    FOUND (302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAT(502, "Bad Gateway");

    public int codeNumber;
    public String message;

    StatusCode(int codeNumber, String message) {
        this.codeNumber = codeNumber;
        this.message = message;
    }
}
