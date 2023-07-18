package utils;

public class StringUtils {
    private static final String NEWLINE = System.getProperty("line.separator");
    private StringUtils() {
    }

    public static String appendNewLine(String string) {
        StringBuilder result = new StringBuilder(string);
        return result.append(NEWLINE).toString();
    }

    public static String appendNewLine(int integer) {
        StringBuilder result = new StringBuilder(integer);
        return result.append(NEWLINE).toString();
    }
}
