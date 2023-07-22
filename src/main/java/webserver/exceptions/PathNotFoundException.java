package webserver.exceptions;

public class PathNotFoundException extends Exception {
    public PathNotFoundException() {
        super("유효하지 않은 경로");
    }
}
