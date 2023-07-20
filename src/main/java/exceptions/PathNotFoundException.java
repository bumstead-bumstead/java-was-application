package exceptions;

public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException() {
        super("유효하지 않은 경로");
    }
}
