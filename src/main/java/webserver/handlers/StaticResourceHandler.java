package webserver.handlers;

import exceptions.PathNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceHandler {
    public static final String TEMPLATES_PATH = "/Users/yohwan/IdeaProjects/be-was/src/main/resources/templates";

    public static byte[] getStaticResource(String resourcePath) throws IOException {
        String fullPath = TEMPLATES_PATH + resourcePath;
        File file = new File(fullPath);

        if (!file.exists() || !file.isFile()) {
            throw new PathNotFoundException();
        }

        return Files.readAllBytes(file.toPath());
    }
}
