package webserver.handlers;

import Application.Controller.Controller;
import webserver.annotations.HandleRequest;
import webserver.annotations.PathParameter;
import webserver.annotations.QueryParameter;
import webserver.exceptions.BadRequestException;
import webserver.exceptions.PathNotFoundException;
import webserver.http.HttpMethodHandlerMapping;
import webserver.http.message.HttpRequest;
import webserver.http.message.HttpResponse;
import webserver.http.message.StatusCode;
import webserver.http.message.URI;

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
        Map<String, String> queryParameters = httpRequest.getBody();
        URI uri = httpRequest.getURI();

        try {
            if (uri.hasExtension()) {
                HttpResponse httpResponse = StaticResourceHandler.handle(httpRequest);
                return httpResponse;
            }

            Method method = requestMappings.getMappedMethod(uri.getPath(), httpRequest.getHttpMethod());
            Object[] argumentValues = extractParameterValues(method, uri.getParameters(), queryParameters);
            HttpResponse httpResponse = (HttpResponse) method.invoke(Controller.getInstance(), argumentValues);

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

    private Object[] extractParameterValues(Method method,
                                            Map<String, String> pathParameters,
                                            Map<String, String> queryParameters) throws BadRequestException {

        //어노테이션이 있는 (꼭 입력해야하는) 매개 변수 리스트
        List<Parameter> arguments = Arrays.stream(method.getParameters())
                .filter(argument -> argument.isAnnotationPresent(PathParameter.class)
                        || argument.isAnnotationPresent(QueryParameter.class))
                .collect(Collectors.toList());

        //필요한 모든 매개 변수를 입력 받았는지 검증
        verifyParameterExistence(pathParameters, queryParameters, arguments);

        //맵에서 찾아서 순서대로 반환
        return arguments.stream().map(argument -> {
            if (argument.isAnnotationPresent(PathParameter.class)) {
                return pathParameters.get(argument.getAnnotation(PathParameter.class).key());
            }
            return queryParameters.get(argument.getAnnotation(QueryParameter.class).key());
        }).toArray();
    }

    private static void verifyParameterExistence(Map<String, String> pathParameters,
                                                 Map<String, String> queryParameters,
                                                 List<Parameter> arguments) throws BadRequestException {
        for (Parameter argument : arguments) {
            if (argument.isAnnotationPresent(PathParameter.class)
                    && !pathParameters.containsKey(argument.getAnnotation(PathParameter.class).key())) {
                throw new BadRequestException("유효하지 않은 매개 변수");
            }

            if (argument.isAnnotationPresent(QueryParameter.class)
                    && !queryParameters.containsKey(argument.getAnnotation(QueryParameter.class).key())) {
                throw new BadRequestException("유효하지 않은 매개 변수");
            }
        }
    }
}