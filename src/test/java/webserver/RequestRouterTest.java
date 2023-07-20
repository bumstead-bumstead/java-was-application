package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RequestRouterTest {

    private HttpMethodHandlerMappings mockedHandlerMappings;
    RequestRouter requestRouter;

    @BeforeEach
    void setUp() {
        requestRouter = new RequestRouter();
    }

    @Test
    @DisplayName("존재하는 정적 리소스를 요청할 시 응답 메세지가 200 OK와 body를 정상적으로 갖는다.")
    void routeStaticResource() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI("/exampleForTest.txt", Map.of(), "txt"),
                "HTTP/1.1",
                new HashMap<>());

        byte[] expectedBody = "test".getBytes();
        StatusCode expectedStatusCode = StatusCode.OK;

        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
        assertArrayEquals(expectedBody, httpResponse.getBody());
    }

    @Test
    @DisplayName("존재하지 않는 정적 리소스를 요청할 시 응답 메세지가 NOT FOUND를 갖고 body를 갖지 않는다.")
    void routeNonexistentStaticResource() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI("/nonexistent.txt", Map.of(), "txt"),
                "HTTP/1.1",
                new HashMap<>());

        StatusCode expectedStatusCode = StatusCode.NOT_FOUND;

        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
        assertEquals(0, httpResponse.getBody().length);
    }

    @Test
    @DisplayName("유효한 URI와 parameter를 요청하면 200 OK를 보낸다.")
    void routeRigthURIAndParameter() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI("/user/create",
                        Map.of("userId", "userId", "password", "password", "name", "name", "email", "email")),
                        "HTTP/1.1",
                        new HashMap<>());

        StatusCode expectedStatusCode = StatusCode.OK;
        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }

    @Test
    @DisplayName("유효하지 않은 URI를 요청하면 Not Found를 보낸다.")
    void routeWrongURI() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI("/nonexistent",
                        Map.of("userId", "userId", "password", "password", "name", "name", "email", "email")),
                "HTTP/1.1",
                new HashMap<>());

        StatusCode expectedStatusCode = StatusCode.NOT_FOUND;
        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }

    @Test
    @DisplayName("유효하지 않은 parameter를 요청하면 BAD REQUEST를 보낸다.")
    void routeWrongParameter() throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI("/user/create",
                        Map.of("password", "password", "name", "name", "email", "email")),
                "HTTP/1.1",
                new HashMap<>());

        StatusCode expectedStatusCode = StatusCode.BAD_REQUEST;
        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }
}