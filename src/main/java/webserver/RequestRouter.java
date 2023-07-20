package webserver;

import Controller.Controller;
import webserver.annotations.HandleRequest;
import webserver.httpMessage.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestRouter {
    private static HttpMethodHandlerMappings requestMappings;

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
        private static final RequestRouter REQUEST_ROUTER = new RequestRouter();
    }

    public static RequestRouter getInstance() {
        return SingletonHelper.REQUEST_ROUTER;
    }

    public HttpResponse route(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException, IOException {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        URI uri = httpRequest.getURI();

        if (uri.hasExtension()) {
            try {
                byte[] body = StaticResourceHandler.getStaticResource(uri.getPath());
                return HttpResponse.generateHttpResponse(StatusCode.OK, body);
            } catch (FileNotFoundException e) {
                return HttpResponse.generateHttpResponse(StatusCode.NOT_FOUND);
            }
        }

        Method method = requestMappings.getMappedMethod(uri.getPath(), httpMethod);
        HttpResponse httpResponse = (HttpResponse) method.invoke(Controller.getInstance(), httpRequest);

        return httpResponse;
    }
}