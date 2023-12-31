package webserver.http.message.parser;

import webserver.http.message.*;
import webserver.http.message.parser.body.RequestBodyParserManager;
import webserver.http.session.Cookie;
import webserver.utils.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.net.URLDecoder.decode;
import static webserver.http.message.HttpHeaderUtils.*;
import static webserver.http.message.URI.*;

public class HttpMessageParser {

    private RequestBodyParserManager requestBodyParserManager;
    private HttpMessageParser() {
        requestBodyParserManager = RequestBodyParserManager.getInstance();
    }

    private static class SingletonHelper {
        private static final HttpMessageParser HTTP_MESSAGE_PARSER = new HttpMessageParser();
    }

    public static HttpMessageParser getInstance() {
        return SingletonHelper.HTTP_MESSAGE_PARSER;
    }

    public ByteArrayOutputStream parseHttpResponse(HttpResponse httpResponse) throws IOException {
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
        HttpMessageHeader headers = httpResponse.getHeaders();
        StringBuilder stringBuilder = new StringBuilder();

        headers.getHeaderMap().forEach((key, value) ->
                stringBuilder.append(key)
                        .append(HEADER_SEPARATOR)
                        .append(StringUtils.appendNewLine(value))
        );

        headers.getCookies().forEach(cookie ->
            stringBuilder.append(SET_COOKIE_HEADER)
                    .append(HEADER_SEPARATOR)
                    .append(StringUtils.appendNewLine(cookie.toHeaderString()))
        );

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

    public HttpRequest parseHttpRequest(InputStream inputStream) throws Exception {
        HttpRequest.Builder httpRequestBuilder = new HttpRequest.Builder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> requestLine = parseRequestLine(bufferedReader);
        HttpMessageHeader headers = parseHeaders(bufferedReader);
        Map<String, String> body = readBody(bufferedReader, headers);

        HttpMethod httpMethod = HttpMethod.valueOf(requestLine.get(0));
        URI uri = parseURI(requestLine.get(1));
        String version = requestLine.get(2);

        return httpRequestBuilder
                .body(body)
                .URI(uri)
                .version(version)
                .httpMethod(httpMethod)
                .headers(headers)
                .build();
    }

    private Map<String, String> readBody(BufferedReader bufferedReader,
                                                HttpMessageHeader headers) throws Exception {
        if (!headers.containsKey(CONTENT_LENGTH_HEADER) || !headers.containsKey(CONTENT_TYPE_HEADER)) {
            return Map.of();
        }

        int contentLength = Integer.parseInt(headers.getValue(CONTENT_LENGTH_HEADER));
        String contentType = headers.getValue(CONTENT_TYPE_HEADER);
        String body = readBody(bufferedReader, contentLength);

        return requestBodyParserManager.parse(body, contentType);
    }

    private static String readBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        bufferedReader.read(buffer, 0, contentLength);

        return decode(new String(buffer));
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
            stringURI = removeQuery(stringURI);
            String extension = parseExtension(stringURI);
            return new URI(path, parameters, MIME.valueOf(extension.toUpperCase()));
        }
        return new URI(path, parameters);
    }

    private static String removeQuery(String path) {
        int index = path.indexOf("?");
        String result;
        if (index != -1) {
            result = path.substring(0, index);
        } else {
            result = path;
        }
        return result;
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

    private static HttpMessageHeader parseHeaders(BufferedReader bufferedReader) throws IOException {
        String line = decode(bufferedReader.readLine());
        HttpMessageHeader.Builder headerBuilder = new HttpMessageHeader.Builder();

        while (isHeaderLeft(line)) {
            line = decode(line);
            String[] field = line.split(HEADER_SEPARATOR + QUERY_SEPARATOR, 2);
            if (REQUEST_COOKIE_HEADER.equals(field[0])) {
                addCookies(headerBuilder, field[1]);
            }
            else {
                headerBuilder.addHeader(field[0], field[1]);
            }
            line = bufferedReader.readLine();
        }

        return headerBuilder.build();
    }

    private static boolean isHeaderLeft(String line) {
        return line != null && !line.equals("");
    }

    private static void addCookies(HttpMessageHeader.Builder headerBuilder, String cookies) {
        Arrays.stream(cookies.split("; "))
                .map((cookie) -> cookie.split("="))
                .forEach((cookieEntry) -> headerBuilder.addCookie(new Cookie(cookieEntry[0], cookieEntry[1])));
    }
}
