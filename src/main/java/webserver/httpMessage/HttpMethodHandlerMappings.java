package webserver.httpMessage;

import webserver.URIHandlers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpMethodHandlerMappings {
    private Map<HttpMethod, URIHandlers> requestMappings;

    public HttpMethodHandlerMappings() {
        this.requestMappings = new HashMap<>();
    }

    /*
    * todo
    *  1. 초기화
    *  2. URIHandlers 초기화
    *  3. Method 반환
    *  4. Method 넣기
    * */

    public void initialize() {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            requestMappings.put(httpMethod, new URIHandlers());
        }
    }

    public void addMapping(String path, HttpMethod httpMethod, Method method) {
        URIHandlers uriHandlers = requestMappings.get(httpMethod);
        uriHandlers.addMapping(path, method);
    }

    public Method getMappedMethod(String path, HttpMethod httpMethod) {
        URIHandlers uriHandlers = requestMappings.get(httpMethod);

        return uriHandlers.getMappedMethod(path);
    }
}
