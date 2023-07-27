package webserver.handlers;

import application.RequestProcessor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.BodyParameter;
import webserver.annotations.HandleRequest;
import webserver.annotations.QueryParameter;
import webserver.exceptions.BadRequestException;
import webserver.exceptions.PathNotFoundException;
import webserver.http.HttpMethodHandlerMapping;
import webserver.http.message.*;
import webserver.http.session.Cookie;
import webserver.http.session.Session;
import webserver.http.session.SessionDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestRouter {
    private final HttpMethodHandlerMapping requestMappings;
    private StaticResourceHandler staticResourceHandler;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestRouter.class);

    private HttpRequestRouter() {
        staticResourceHandler = StaticResourceHandler.getInstance();
        requestMappings = new HttpMethodHandlerMapping();
        requestMappings.initialize();

        Method[] methods = RequestProcessor.class.getDeclaredMethods();

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

    public HttpResponse route(HttpRequest httpRequest) {
        Map<String, String> bodyParameters = httpRequest.getBody();
        URI uri = httpRequest.getURI();
        Session session = getSession(httpRequest);

        try {
            //매핑된 메소드가 존재하는 경우, 해당 메소드가 처리
            if (requestMappings.containsCapableMethod(uri.getPath(), httpRequest.getHttpMethod())) {
                Method method = requestMappings.getMappedMethod(uri.getPath(), httpRequest.getHttpMethod());
                Object[] argumentValues = getRequiredParameters(bodyParameters, uri, session, method);
                return executeMethod(method, argumentValues);
            }
            //정적인 리소스로 취급하고 처리
            return staticResourceHandler.handle(httpRequest);
        } catch (PathNotFoundException e) {
            return HttpResponse.generateError(StatusCode.NOT_FOUND);
        } catch (BadRequestException e) {
            return HttpResponse.generateError(StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return HttpResponse.generateError(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpResponse executeMethod(Method method, Object[] argumentValues) throws IllegalAccessException, InvocationTargetException {
        return (HttpResponse) method.invoke(RequestProcessor.getInstance(), argumentValues);
    }

    private static Object[] addSessionToArgument(Session session, Object[] argumentValues) {
        Object[] argumentsWithSession = Arrays.copyOf(argumentValues, argumentValues.length + 1);
        argumentsWithSession[argumentsWithSession.length - 1] = session;
        return argumentsWithSession;
    }

    private Session getSession(HttpRequest httpRequest) {
        HttpMessageHeader header = httpRequest.getHeaders();
        Cookie sessionCookie = header.getCookie("sid");
        if (sessionCookie == null) {
            return null;
        }
        return SessionDatabase.findSession(sessionCookie.getValue());
    }

    private Object[] getRequiredParameters(Map<String, String> bodyParameters, URI uri, Session session, Method method) throws BadRequestException {
        Object[] argumentValues = extractParameterValues(method, uri.getParameters(), bodyParameters);
        if (requireSession(method)) {
            argumentValues = addSessionToArgument(session, argumentValues);
        }
        return argumentValues;
    }

    private boolean requireSession(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .anyMatch(parameterType -> parameterType == Session.class);
    }

    private Object[] extractParameterValues(Method method,
                                            Map<String, String> pathParameters,
                                            Map<String, String> queryParameters) throws BadRequestException {

        //어노테이션이 있는 (꼭 입력해야하는) 매개 변수 리스트
        List<Parameter> arguments = Arrays.stream(method.getParameters())
                .filter(argument -> argument.isAnnotationPresent(QueryParameter.class)
                        || argument.isAnnotationPresent(BodyParameter.class))
                .collect(Collectors.toList());

        //필요한 모든 매개 변수를 입력 받았는지 검증
        verifyParameterExistence(pathParameters, queryParameters, arguments);

        //맵에서 찾아서 순서대로 반환
        return arguments.stream().map(argument -> {
            if (argument.isAnnotationPresent(QueryParameter.class)) {
                return pathParameters.get(argument.getAnnotation(QueryParameter.class).key());
            }
            return queryParameters.get(argument.getAnnotation(BodyParameter.class).key());
        }).toArray();
    }

    private static void verifyParameterExistence(Map<String, String> pathParameters,
                                                 Map<String, String> queryParameters,
                                                 List<Parameter> arguments) throws BadRequestException {
        for (Parameter argument : arguments) {
            if (argument.isAnnotationPresent(QueryParameter.class)
                    && !pathParameters.containsKey(argument.getAnnotation(QueryParameter.class).key())) {
                throw new BadRequestException("유효하지 않은 매개 변수");
            }

            if (argument.isAnnotationPresent(BodyParameter.class)
                    && !queryParameters.containsKey(argument.getAnnotation(BodyParameter.class).key())) {
                throw new BadRequestException("유효하지 않은 매개 변수");
            }
        }
    }
}