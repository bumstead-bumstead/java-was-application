package webserver.handlers;

import Application.Controller.Controller;
import webserver.exceptions.BadRequestException;
import webserver.exceptions.PathNotFoundException;
import webserver.annotations.HandleRequest;
import webserver.annotations.QueryParameter;
import webserver.http.HttpMethodHandlerMapping;
import webserver.http.message.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestRouter {
    //set default for test
    static HttpMethodHandlerMapping requestMappings;

    static {
        requestMappings = new HttpMethodHandlerMapping();
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
                HttpResponse httpResponse = StaticResourceHandler.handle(httpRequest);
                return httpResponse;
            }

            Method method = requestMappings.getMappedMethod(uri.getPath(), httpMethod);

            Object[] parameters = extractParameterValues(method, uri.getParameters());

            HttpResponse httpResponse = (HttpResponse) method.invoke(Controller.getInstance(), parameters);

            return httpResponse;
        } catch (PathNotFoundException e) {
            return new HttpResponse.Builder()
                    .statusCode(StatusCode.NOT_FOUND)
                    .build();
        } catch (BadRequestException e) {
            return new HttpResponse.Builder()
                    .statusCode(StatusCode.BAD_REQUEST)
                    .build();
        }
    }

    private Object[] extractParameterValues(Method method, Map<String, String> inputs) throws BadRequestException {
        List<Parameter> parameters = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(QueryParameter.class))
                .collect(Collectors.toList());

        verifyParameterExistence(inputs, parameters);

        return parameters.stream().map(parameter -> inputs.get(parameter.getAnnotation(QueryParameter.class).key())).toArray();
    }

    private static void verifyParameterExistence(Map<String, String> inputs, List<Parameter> parameters) throws BadRequestException {
        for (Parameter parameter : parameters) {
            if (!inputs.containsKey(parameter.getAnnotation(QueryParameter.class).key())) {
                throw new BadRequestException("유효하지 않은 매개 변수");
            }
        }
    }
}