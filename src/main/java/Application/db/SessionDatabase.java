package Application.db;

import Application.model.Session;
import webserver.exceptions.BadRequestException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static void addSession(Session session) throws BadRequestException {
        sessionMap.put(session.getId(), session);
    }

    public static boolean containsSession(String sessionId) {
        return sessionMap.containsKey(sessionId);
    }

    public static Session findSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static int size() {
        return sessionMap.size();
    }

    public static void clear() {
        sessionMap.clear();
    }
}
