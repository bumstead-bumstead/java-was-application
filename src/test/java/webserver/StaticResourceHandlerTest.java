package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.exceptions.PathNotFoundException;
import webserver.handlers.StaticResourceHandler;
import webserver.http.message.*;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static webserver.http.message.HttpHeaderUtils.CONTENT_TYPE_HEADER;
public class StaticResourceHandlerTest {

    private StaticResourceHandler staticResourceHandler;

    @BeforeEach
    void setUp() {
        staticResourceHandler = StaticResourceHandler.getInstance();
    }

    @Test
    @DisplayName("존재하는 경로의 정적 리소스의 경우 바이트 파일을 반환한다.")
    void getStaticResourceExisting() throws IOException, PathNotFoundException {
        String resourcePath = "/exampleForTest.txt";
        URI uri = new URI(resourcePath, Map.of(), MIME.TXT);
        byte[] content = staticResourceHandler.getResourceForTest(uri);
        assertNotNull(content);
        assertTrue(content.length > 0);
    }

    @Test
    @DisplayName("존재하지 않는 경로의 정적 리소스 요청은 PathNotFoundException을 발생시킨다.")
    void getStaticResourceNonExisting() {
        String resourcePath = "/non_existing.txt";
        URI uri = new URI(resourcePath, Map.of(), MIME.TXT);

        assertThatThrownBy(() -> staticResourceHandler.getResourceForTest(uri))
                .isInstanceOf(PathNotFoundException.class);
    }

    @ParameterizedTest
    @MethodSource("provideContentTypeForTestingHandle")
    @DisplayName("요청 URI의 확장자에 따라서 응답의 ContentType을 변경한다.")
    void handleTest(String path, MIME mime) throws IOException, PathNotFoundException {

        HttpRequest httpRequest = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI(path, Map.of(), mime))
                .version("HTTP/1.1")
                .build();

        HttpResponse httpResponse = staticResourceHandler.handle(httpRequest);
        HttpMessageHeader headers = httpResponse.getHeaders();
        assertThat(mime.contentType).isEqualTo(headers.getValue(CONTENT_TYPE_HEADER));
    }

    public static Stream<Arguments> provideContentTypeForTestingHandle() {
        return Stream.of(
                Arguments.of("/css/styles.css", MIME.CSS),
                Arguments.of("/fonts/glyphicons-halflings-regular.woff", MIME.WOFF),
                Arguments.of("/images/80-text.png", MIME.PNG),
                Arguments.of("/favicon.ico", MIME.ICO)
        );
    }
}
