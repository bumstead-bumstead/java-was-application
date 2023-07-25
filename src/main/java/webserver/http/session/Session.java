package webserver.http.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private Map<String, Object> attributes;
    private String id;

    public Session() {
        this.attributes = new ConcurrentHashMap<>();
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
