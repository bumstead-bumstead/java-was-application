package exceptions;

public class IllegalParameterException extends NullPointerException {
    public IllegalParameterException() {
        super("유효하지 않은 매개변수");
    }
}
