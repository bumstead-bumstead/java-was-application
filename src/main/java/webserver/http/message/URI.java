package webserver.http.message;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class URI {
    public static final String QUERY_SEPARATOR = "?";
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String SPACE = " ";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String PARAMETER_EQUAL_SIGN = "=";
    public static final String EXTENSION_SEPARATOR = ".";

    private final String path;
    private final Map<String, String> parameters;
    private final Optional<MIME> extension;

    public URI(String path, Map<String, String> parameters) {
        this.path = path;
        this.parameters = parameters;
        this.extension = Optional.empty();
    }

    public URI(String path, Map<String, String> parameters, MIME extension) {
        this.path = path;
        this.parameters = parameters;
        this.extension = Optional.of(extension);
    }

    public boolean hasMIMEForStaticResource() {
        return extension.isPresent() && !extension.get().isTemplate();
    }

    public boolean hasParameter() {
        return !parameters.isEmpty();
    }

    public boolean hasExtension() {
        return extension.isPresent();
    }

    ;


    public String getPath() {
        return path;
    }

    public Optional<MIME> getExtension() {
        return extension;
    }

    public Map<String, String> getParameters() {
        return parameters;
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
