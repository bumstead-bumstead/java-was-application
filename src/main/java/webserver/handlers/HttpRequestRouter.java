package webserver.handlers;

import Controller.Controller;
import exceptions.PathNotFoundException;
import webserver.annotations.HandleRequest;
import webserver.http.*;
import webserver.http.message.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpRequestRouter {
    //set default for test
    static HttpMethodHandlerMappings requestMappings;

    static {
        requestMappings = new HttpMethodHandlerMappings();
        requestMappings.initialize();

        Method[] methods = Controller.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(HandleRequest.class)) {
                HandleRequest handleRequest = method.getAnnotation(HandleRequest.class);
                requestMappings.addMapping(handleRequest.path(), handleRequest.httpMethod(), method);
            }
        }
    }

    private static class SingletonHelper {
        private static final HttpRequestRouter REQUEST_ROUTER = new HttpRequestRouter();
    }

    public static HttpRequestRouter getInstance() {
        return SingletonHelper.REQUEST_ROUTER;
    }

    public HttpResponse route(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, IOException {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        URI uri = httpRequest.getURI();

        try {
            if (uri.hasExtension()) {
                byte[] body = StaticResourceHandler.getStaticResource(uri.getPath());
                return HttpResponse.generateHttpResponse(StatusCode.OK, body);
            }
            Method method = requestMappings.getMappedMethod(uri.getPath(), httpMethod);
            HttpResponse httpResponse = (HttpResponse) method.invoke(Controller.getInstance(), httpRequest);

            return httpResponse;
        } catch (PathNotFoundException e) {
            return HttpResponse.generateHttpResponse(StatusCode.NOT_FOUND);
        }
    }
}