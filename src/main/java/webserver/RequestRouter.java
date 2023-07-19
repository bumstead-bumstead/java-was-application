package webserver;

import Controller.Controller;
import webserver.annotations.HandleRequest;
import webserver.httpMessage.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestRouter {
    private static Map<HttpMethod, URIHandlers> requestMappings;

    static {
        requestMappings = new HashMap<>();

        for (HttpMethod httpMethod : HttpMethod.values()) {
            requestMappings.put(httpMethod, new URIHandlers());
        }

        Method[] methods = Controller.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(HandleRequest.class)) {
                HandleRequest handleRequest = method.getAnnotation(HandleRequest.class);
                URIHandlers uriHandlers = requestMappings.get(handleRequest.httpMethod());
                uriHandlers.addMapping(handleRequest.path(), method);
            }
        }

    }

    public static HttpResponse route(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, IOException {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        URI uri = httpRequest.getURI();

        if (uri.hasExtension()) {
            byte[] body = httpRequest.getBytesOfGetRequest();
            return HttpResponse.generateHttpResponse(StatusCode.OK, body);
        }

        Method method = requestMappings.get(httpMethod).getMappedMethod(uri.getPath());
        HttpResponse httpResponse = (HttpResponse) method.invoke(null, httpRequest);

        return httpResponse;
    }
}