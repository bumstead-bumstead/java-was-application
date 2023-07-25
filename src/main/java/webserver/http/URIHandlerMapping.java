package webserver.http;

import webserver.exceptions.PathNotFoundException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class URIHandlerMapping {
    private Map<String, Method> map;

    public URIHandlerMapping() {
        this.map = new HashMap<>();
    }

    public void addMapping(String path, Method method) {
        map.put(path, method);
    }

    public Method getMappedMethod(String path) throws PathNotFoundException {
        verifyMappedMethodExistence(path);
        return map.get(path);
    }

    public boolean contains(String path) {
        return map.containsKey(path);
    }

    private void verifyMappedMethodExistence(String path) throws PathNotFoundException {
        if (!map.containsKey(path)) {
            throw new PathNotFoundException();
        }
    }

    @Override
    public String toString() {
        return "URIHandlers{" +
                "map=" + map +
                '}';
    }
}
