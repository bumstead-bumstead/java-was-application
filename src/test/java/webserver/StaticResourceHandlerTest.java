package webserver;

import exceptions.PathNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.handlers.StaticResourceHandler;
import webserver.http.message.MIME;
import webserver.http.message.URI;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
}
