package webserver;

import exceptions.PathNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.handlers.StaticResourceHandler;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class StaticResourceHandlerTest {

    @Test
    @DisplayName("존재하는 경로의 정적 리소스의 경우 바이트 파일을 반환한다.")
    public void getStaticResourceExisting() throws IOException {
        String resourcePath = "/exampleForTest.txt";
        byte[] content = StaticResourceHandler.getStaticResource(resourcePath);
        assertNotNull(content);
        assertTrue(content.length > 0);
    }

    @Test
    @DisplayName("존재하지 않는 경로의 정적 리소스 요청은 PathNotFoundException을 발생시킨다.")
    public void getStaticResourceNonExisting() {
        String resourcePath = "/non_existing.txt";
        assertThrows(PathNotFoundException.class, () -> {
            StaticResourceHandler.getStaticResource(resourcePath);
        });
    }
}
