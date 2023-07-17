package model.Domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
    private HttpMethod method;
    private String URI;
    private String version;
    private Map<String, String> metadata;

    private HttpRequestHeader() {
    }

    public HttpRequestHeader(HttpMethod method, String URI, String version, Map<String, String> metadata) {
        this.method = method;
        this.URI = URI;
        this.version = version;
        this.metadata = metadata;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public static HttpRequestHeader createaHttpRequestHeaderWithBufferedReader(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();

        String[] requestLine = line.split(" ");
        HttpMethod method = HttpMethod.valueOf(requestLine[0]);
        String URI = requestLine[1];
        String version = requestLine[2];

        line = bufferedReader.readLine();

        Map<String, String> metadata = new HashMap<>();

        while (line != null && !line.equals("")) {
            String[] field = line.split(": ");
            metadata.put(field[0], field[1]);

            line = bufferedReader.readLine();
        }

        return new HttpRequestHeader(method, URI, version, metadata);
    }
}
