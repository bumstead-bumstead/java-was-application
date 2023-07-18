package webserver.message;

public enum StatusCode {
    OK (200),
    CREATED (201),
    ACCEPTED (202),
    MULTIPLE_CHOICE (300),
    MOVED_PERMANENTLY (301),
    FOUND (302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAT(502);

    public int codeNumber;

    StatusCode(int statusCodeNumber) {
        this.codeNumber = statusCodeNumber;
    }
}
