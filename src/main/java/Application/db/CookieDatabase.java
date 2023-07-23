package Application.db;

import Application.model.Cookie;
import webserver.exceptions.BadRequestException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieDatabase {
    private static Map<String, Cookie> cookies = new ConcurrentHashMap<>();

    public static void addCookie(Cookie cookie) throws BadRequestException {
        Cookie existingCookie = cookies.putIfAbsent(cookie.getValue(), cookie);
        verifyDuplicatedInput(existingCookie);
    }

    public static void clear() {
        cookies.clear();
    }

    private static void verifyDuplicatedInput(Cookie existingCookie) throws BadRequestException {
        if (existingCookie != null) {
            throw new BadRequestException("이미 존재하는 유저 정보");
        }
    }

    public static Cookie findCookieByValue(String value) {
        return cookies.get(value);
    }
}
