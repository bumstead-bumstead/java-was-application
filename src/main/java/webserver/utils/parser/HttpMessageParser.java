package webserver.utils.parser;

import webserver.http.message.*;
import webserver.utils.HttpHeaderUtils;
import webserver.utils.StringUtils;
import webserver.utils.parser.body.RequestBodyParserManager;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static webserver.http.message.URI.*;

public class HttpMessageParser {


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
                    .append(HEADER_SEPARATOR)
                    .append(StringUtils.appendNewLine(line.getValue()));
        }
        stringBuilder.append(StringUtils.appendNewLine());

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

    public static HttpRequest parseHttpRequest(InputStream inputStream) throws Exception {
        HttpRequest.Builder httpRequestBuilder = new HttpRequest.Builder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> requestLine = parseRequestLine(bufferedReader);
        Map<String, String> headers = parseHeaders(bufferedReader);
        Map<String, String> body = readBody(bufferedReader, headers);

        HttpMethod httpMethod = HttpMethod.valueOf(requestLine.get(0));
        URI uri = parseURI(requestLine.get(1));
        String version = requestLine.get(2);

        return httpRequestBuilder
                .body(body)
                .URI(uri)
                .version(version)
                .httpMethod(httpMethod)
                .build();
    }

    private static Map<String, String> readBody(BufferedReader bufferedReader,
                                                Map<String, String> headers) throws Exception {
        if (!headers.containsKey(HttpHeaderUtils.CONTENT_LENGTH_HEADER)
                || !headers.containsKey(HttpHeaderUtils.CONTENT_TYPE_HEADER)) {
            return Map.of();
        }

        int contentLength = Integer.parseInt(headers.get(HttpHeaderUtils.CONTENT_LENGTH_HEADER));
        String contentType = headers.get(HttpHeaderUtils.CONTENT_TYPE_HEADER);

        String body = readBody(bufferedReader, contentLength);

        return RequestBodyParserManager.parse(body, contentType);
    }

    private static String readBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        bufferedReader.read(buffer, 0, contentLength);

        return new String(buffer);
    }

    private static URI parseURI(String stringURI) {
        Map<String, String> parameters = new HashMap<>();
        String path = stringURI;

        if (hasQuery(stringURI)) {
            int queryStartIndex = stringURI.indexOf(QUERY_SEPARATOR);
            path = stringURI.substring(0, queryStartIndex);
            parameters = parseParameters(stringURI);
        }
        if (hasExtension(path)) {
            String extension = parseExtension(stringURI);
            return new URI(path, parameters, MIME.valueOf(extension.toUpperCase()));
        }
        return new URI(path, parameters);
    }

    private static String parseExtension(String stringURI) {
        int dotIndex = stringURI.lastIndexOf(EXTENSION_SEPARATOR);
        String extension = stringURI.substring(dotIndex + 1);

        return extension;
    }

    private static boolean hasExtension(String stringURI) {
        return stringURI.contains(EXTENSION_SEPARATOR);
    }

    private static boolean hasQuery(String stringURI) {
        return stringURI.contains(QUERY_SEPARATOR);
    }

    public static Map<String, String> parseParameters(String stringUri) {
        Map<String, String> parameters = new HashMap<>();
        int queryStartIndex = stringUri.indexOf(QUERY_SEPARATOR) + 1;
        String query = stringUri.substring(queryStartIndex);

        List<String> queries = parseStringToList(query, PARAMETER_SEPARATOR);

        for (String line : queries) {
            String[] parameter = line.split(PARAMETER_EQUAL_SIGN);
            parameters.put(parameter[0], parameter[1]);
        }
        return parameters;
    }

    private static List<String> parseRequestLine(BufferedReader bufferedReader) throws IOException {
        String stringRequestLine = bufferedReader.readLine();
        String[] requestLineArray = stringRequestLine.split(SPACE);
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }

    private static List<String> parseStringToList(String requestLine, String separator) {
        String[] requestLineArray = requestLine.split(separator);
        List<String> result = Arrays.stream(requestLineArray).collect(Collectors.toList());

        return result;
    }

    private static Map<String, String> parseHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = bufferedReader.readLine();

        while (line != null && !line.equals("")) {
            String[] field = line.split(HEADER_SEPARATOR + QUERY_SEPARATOR, 2);
            headers.put(field[0], field[1]);
            line = bufferedReader.readLine();
        }
        return headers;
    }
}
