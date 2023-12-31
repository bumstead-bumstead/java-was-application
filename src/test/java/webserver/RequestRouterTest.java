package webserver;

import application.db.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.handlers.HttpRequestRouter;
import webserver.http.message.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class RequestRouterTest {

    HttpRequestRouter requestRouter;

    @BeforeEach
    void setUp() {
        UserDatabase.clear();
        requestRouter = HttpRequestRouter.getInstance();
    }

    @ParameterizedTest
    @MethodSource("providePathAndStatusCodeAndBodyForTestingStaticResource")
    @DisplayName("정적 리소스에 대한 요청 처리 테스트")
    void routeStaticResource(String path, StatusCode expectedStatusCode, byte[] expectedBody) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpRequest httpRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI(path, Map.of(), MIME.TXT))
                .version("HTTP/1.1")
                .build();

        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertThat(httpResponse.getStatusCode()).isEqualTo(expectedStatusCode);
        assertThat(expectedBody).isEqualTo(httpResponse.getBody());
    }

    public static Stream<Arguments> providePathAndStatusCodeAndBodyForTestingStaticResource() {
        return Stream.of(
                Arguments.of("/exampleForTest.txt", StatusCode.OK, "test".getBytes()),
                Arguments.of("/nonexistent.txt", StatusCode.NOT_FOUND, new byte[]{})
        );
    }

    @ParameterizedTest
    @MethodSource("providePathAndParametersAndStatusCodeForTestingURIAndParameters")
    @DisplayName("URI, parameter에 대한 요청 처리 테스트")
    void routeURIAndParameter(String path, Map<String, String> parameters, StatusCode expectedStatusCode) throws Exception {
        HttpRequest httpRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.POST)
                .URI(new URI(path, Map.of()))
                .body(parameters)
                .version("HTTP/1.1")
                .build();

        HttpResponse httpResponse = requestRouter.route(httpRequest);

        assertThat(httpResponse.getStatusCode()).isEqualTo(expectedStatusCode);
    }

    public static Stream<Arguments> providePathAndParametersAndStatusCodeForTestingURIAndParameters() {
        return Stream.of(
                Arguments.of("/user/create", Map.of("userId", "userId", "password", "password", "name", "name", "email", "email"), StatusCode.FOUND),
                Arguments.of("/nonexistent", Map.of("userId", "userId", "password", "password", "name", "name", "email", "email"), StatusCode.NOT_FOUND),
                Arguments.of("/user/create", Map.of("password", "password", "name", "name", "email", "email"), StatusCode.BAD_REQUEST)
        );
    }
}