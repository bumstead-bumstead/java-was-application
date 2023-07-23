package webserver.utils;

public class HttpHeaderUtils {
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";
    public static final String LOCATION_HEADER = "Location";
    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    public static final String PATH_HEADER = "path=";
    public static final String COOKIE_VALUE_PREFIX = "sid=";
    public static final int COOKIE_LENGTH = 10;

    public static String generateCookieHeader() {
        String cookie = RandomUtils.generateRandomString(COOKIE_LENGTH);

        StringBuilder cookieValue = new StringBuilder()
                .append(COOKIE_VALUE_PREFIX)
                .append(cookie)
                .append("; ")
                .append(PATH_HEADER)
                .append("/");

        return cookieValue.toString();
    }
}
