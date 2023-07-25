package application.model;

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

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
