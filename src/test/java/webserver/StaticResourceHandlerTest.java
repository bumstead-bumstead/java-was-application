package webserver;

import webserver.exceptions.PathNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.handlers.StaticResourceHandler;
import webserver.http.message.*;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static webserver.handlers.StaticResourceHandler.handle;
import static webserver.utils.HttpHeaderUtils.CONTENT_TYPE_HEADER;
public class StaticResourceHandlerTest {

    @Test
    @DisplayName("존재하는 경로의 정적 리소스의 경우 바이트 파일을 반환한다.")
    void getStaticResourceExisting() throws IOException {
        String resourcePath = "/exampleForTest.txt";
        URI uri = new URI(resourcePath, Map.of(), MIME.TXT);
        byte[] content = StaticResourceHandler.getResourceForTest(uri);
        assertNotNull(content);
        assertTrue(content.length > 0);
    }

    @Test
    @DisplayName("존재하지 않는 경로의 정적 리소스 요청은 PathNotFoundException을 발생시킨다.")
    void getStaticResourceNonExisting() {
        String resourcePath = "/non_existing.txt";
        URI uri = new URI(resourcePath, Map.of(), MIME.TXT);
        assertThrows(PathNotFoundException.class, () -> {
            StaticResourceHandler.getResourceForTest(uri);
        });
    }

    @ParameterizedTest
    @MethodSource("provideContentTypeForTestingHandle")
    @DisplayName("요청 URI의 확장자에 따라서 응답의 ContentType을 변경한다.")
    void handleTest(String path, MIME mime) throws IOException {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.GET,
                new URI(path,
                        Map.of(),
                        mime),
                "HTTP/1.1",
                Map.of());

        HttpResponse httpResponse = handle(httpRequest);
        Map<String, String> headers = httpResponse.getHeaders();
        assertEquals(headers.get(CONTENT_TYPE_HEADER), mime.contentType);
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
