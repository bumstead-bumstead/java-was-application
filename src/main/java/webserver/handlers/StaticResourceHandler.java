package webserver.handlers;

import webserver.exceptions.PathNotFoundException;
import webserver.http.message.*;
import webserver.utils.HttpHeaderUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class StaticResourceHandler {
    public static final String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public static final String TEMPLATES_PATH = "/src/main/resources/templates";
    public static final String STATIC_PATH = "/src/main/resources/static";

    public static HttpResponse handle(HttpRequest httpRequest) throws IOException {
        URI uri = httpRequest.getURI();

        byte[] body = getResource(uri);
        MIME mime = uri.getExtension().get();

        Map<String, String> headers = Map.of(HttpHeaderUtils.CONTENT_TYPE_HEADER,
                mime.contentType,
                HttpHeaderUtils.CONTENT_LENGTH_HEADER,
                String.valueOf(body.length));

        return new HttpResponse.Builder()
                .headers(headers)
                .body(body)
                .build();
    }

    private static byte[] getResource(URI uri) throws IOException {
        String fullPath = PROJECT_ROOT_PATH + TEMPLATES_PATH + uri.getPath();
        if (uri.hasMIMEForStaticResource()) {
            fullPath = PROJECT_ROOT_PATH + STATIC_PATH + uri.getPath();
        }
        return readFile(fullPath);
    }

    private static byte[] readFile(String path) throws IOException {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            throw new PathNotFoundException();
        }

        return Files.readAllBytes(file.toPath());
    }

    public static byte[] getResourceForTest(URI uri) throws IOException {
        String fullPath = PROJECT_ROOT_PATH + TEMPLATES_PATH + uri.getPath();
        if (uri.hasMIMEForStaticResource()) {
            fullPath = PROJECT_ROOT_PATH + STATIC_PATH + uri.getPath();
        }
        return readFile(fullPath);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }
}
