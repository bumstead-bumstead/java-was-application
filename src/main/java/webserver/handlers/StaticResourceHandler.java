package webserver.handlers;

import webserver.exceptions.PathNotFoundException;
import webserver.http.message.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceHandler {
    public static final String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    public static final String TEMPLATES_PATH = "/src/main/resources/templates";
    public static final String STATIC_PATH = "/src/main/resources/static";

    public static class SingletonHelper {
        private static final StaticResourceHandler STATIC_RESOURCE_HANDLER = new StaticResourceHandler();
    }

    private StaticResourceHandler() {
    }

    public static StaticResourceHandler getInstance() {
        return SingletonHelper.STATIC_RESOURCE_HANDLER;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws IOException, PathNotFoundException {
        URI uri = httpRequest.getURI();

        byte[] body = getResource(uri);
        MIME mime = uri.getExtension().get();

        HttpMessageHeader headers = new HttpMessageHeader.Builder()
                .addContentLength(body.length)
                .addContentType(mime.contentType)
                .build();

        return new HttpResponse.Builder()
                .headers(headers)
                .body(body)
                .build();
    }

    private byte[] getResource(URI uri) throws IOException, PathNotFoundException {
        String fullPath = PROJECT_ROOT_PATH + TEMPLATES_PATH + uri.getPath();
        if (uri.hasMIMEForStaticResource()) {
            fullPath = PROJECT_ROOT_PATH + STATIC_PATH + uri.getPath();
        }
        return readFile(fullPath);
    }

    private byte[] readFile(String path) throws IOException, PathNotFoundException {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            throw new PathNotFoundException();
        }

        return Files.readAllBytes(file.toPath());
    }

    public byte[] getResourceForTest(URI uri) throws IOException, PathNotFoundException {
        String fullPath = PROJECT_ROOT_PATH + TEMPLATES_PATH + uri.getPath();
        if (uri.hasMIMEForStaticResource()) {
            fullPath = PROJECT_ROOT_PATH + STATIC_PATH + uri.getPath();
        }
        return readFile(fullPath);
    }
}
