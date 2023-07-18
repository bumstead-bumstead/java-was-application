package webserver.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URI uri = (URI) o;
        return Objects.equals(path, uri.path) && Objects.equals(parameters, uri.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, parameters);
    }
}
