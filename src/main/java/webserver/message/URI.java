package webserver.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.HttpMessageParser.*;

public class URI {
    public static final String QUERY_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";
    private final String path;
    private final Map<String, String> parameters;

    public URI(String path, Map<String, String> parameters) {
        this.path = path;
        this.parameters = parameters;
    }

    public boolean hasParameter() {
        return !parameters.isEmpty();
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "URI{" +
                "path='" + path + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
