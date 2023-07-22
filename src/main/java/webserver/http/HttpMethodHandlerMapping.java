package webserver.http;

import webserver.exceptions.PathNotFoundException;
import webserver.http.message.HttpMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpMethodHandlerMapping {
    private Map<HttpMethod, URIHandlerMapping> requestMappings;

    public HttpMethodHandlerMapping() {
        this.requestMappings = new HashMap<>();
    }

    public void initialize() {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            requestMappings.put(httpMethod, new URIHandlerMapping());
        }
    }

    public void addMapping(String path, HttpMethod httpMethod, Method method) {
        URIHandlerMapping uriHandlers = requestMappings.get(httpMethod);
        uriHandlers.addMapping(path, method);
    }

    public Method getMappedMethod(String path, HttpMethod httpMethod) throws PathNotFoundException {
        URIHandlerMapping uriHandlers = requestMappings.get(httpMethod);

        return uriHandlers.getMappedMethod(path);
    }
}
