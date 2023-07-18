package utils;

import webserver.message.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static webserver.message.URI.PARAMETER_SEPARATOR;
import static webserver.message.URI.QUERY_SEPARATOR;

public class HttpMessageParser {

    public static final String SPACE = " ";

    public static ByteArrayOutputStream parseHttpResponse(HttpResponse httpResponse) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        fillStatusLine(httpResponse, outputStream);
        fillHeaders(httpResponse, outputStream);
        fillBody(httpResponse, outputStream);

        return outputStream;
    }

    private static void fillBody(HttpResponse httpResponse, OutputStream outputStream) throws IOException {
        byte[] body = httpResponse.getBody();

        outputStream.write(body);
    }

    private static void fillHeaders(HttpResponse httpResponse, OutputStream outputStream) throws IOException {
        Map<String, String> headers = httpResponse.getHeaders();
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> line : headers.entrySet()) {
            stringBuilder.append(line.getKey())
                    .append(": ")
                    .append(StringUtils.appendNewLine(line.getValue()));
        }
        stringBuilder.append(StringUtils.appendNewLine(""));

        String stringHeader = stringBuilder.toString();
        outputStream.write(stringHeader.getBytes());
    }

    private static void fillStatusLine(HttpResponse httpResponse, OutputStream outputStream) throws IOException {
        StatusCode statusCode = httpResponse.getStatusCode();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(httpResponse.getVersion())
                .append(SPACE)
                .append(statusCode.codeNumber)
                .append(SPACE)
                .append(StringUtils.appendNewLine(statusCode.name()));

        String statusLine = stringBuilder.toString();
        outputStream.write(statusLine.getBytes());
    }

    public static HttpRequest parseHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> requestHeaderList = parseBufferedReader(bufferedReader);
        List<String> requestLine = parseRequestLine(requestHeaderList.remove(0));

        HttpMethod method = HttpMethod.valueOf(requestLine.get(0));
        URI uri = parseURI(requestLine.get(1));
        String version = requestLine.get(2);

        Map<String, String> metadata = parseHeaders(requestHeaderList);

        return new HttpRequest(method, uri, version, metadata);
    }

    private static URI parseURI(String stringURI) {
        Map<String, String> parameters = new HashMap<>();

        String[] uriArray = stringURI.split("\\?");
        String path = uriArray[0];

        if (stringURI.contains(QUERY_SEPARATOR)) {
            String query = uriArray[1];
            List<String> queryList = parseStringToList(query, PARAMETER_SEPARATOR);

            parameters = parseHeaders(queryList);
        }

        return new URI(path, parameters);
    }

    public static List<String> parseBufferedReader(BufferedReader bufferedReader) throws IOException {
        List<String> result = new ArrayList<>();
        String line = bufferedReader.readLine();

        while (line != null && !line.equals("")) {
            result.add(line);
            line = bufferedReader.readLine();
        }
        return result;
    }

    public static List<String> parseRequestLine(String requestLine) {
        String[] requestLineArray = requestLine.split(" ");
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }

    public static List<String> parseStringToList(String requestLine, String separator) {
        String[] requestLineArray = requestLine.split(separator);
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }

    public static Map<String, String> parseHeaders(List<String> requestHeaderList) {
        Map<String, String> metadata = new HashMap<>();

        for (String line : requestHeaderList) {
            String[] field = line.split(":", 2);
            metadata.put(removeSpaces(field[0]), removeSpaces(field[1]));
        }

        return metadata;
    }

    private static String removeSpaces(String input) {
        return input.replaceAll("\\s", "");
    }
}
