package webserver;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class StaticResourceHandlerTest {

    @Test
    public void testGetStaticResource_ExistingResource() throws IOException {
        String resourcePath = "/exampleForTest.txt"; // 존재하는 리소스 파일 경로
        byte[] content = StaticResourceHandler.getStaticResource(resourcePath);
        assertNotNull(content);
        assertTrue(content.length > 0);
    }

    @Test
    public void testGetStaticResource_NonExistingResource() {
        String resourcePath = "/non_existing.txt"; // 존재하지 않는 리소스 파일 경로
        assertThrows(FileNotFoundException.class, () -> {
            StaticResourceHandler.getStaticResource(resourcePath);
        });
    }
}
