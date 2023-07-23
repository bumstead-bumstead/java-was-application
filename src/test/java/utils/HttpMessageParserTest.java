package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.message.*;
import webserver.utils.parser.HttpMessageParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpMessageParserTest {

    @Test
    @DisplayName("HttpResponse to OutputStream 파싱 로직 테스트")
    void parseHttpResponse() throws IOException {
        //given
        HttpResponse httpResponse = new HttpResponse.Builder().build();//new HttpResponse(StatusCode.OK, new byte[0]);

        //when
        OutputStream outputStream = HttpMessageParser.parseHttpResponse(httpResponse);
        String output = outputStream.toString();

        //then
        assertThat(output.contains("Content-Length: 0"));
        assertThat(output.contains("Content-Type: text/html;charset=utf-8"));
        assertThat(output.startsWith("HTTP/1.1 200 OK"));
    }

    @Test
    @DisplayName("InputStream to HttpRequest 파싱 로직 테스트")
    void parseHttpRequest() throws Exception {
        //given
        String input = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n";

        Map<String, String> expectedHeader = new HashMap<>();
        expectedHeader.put("Host", "localhost:8080");
        expectedHeader.put("Connection", "keep-alive");
        expectedHeader.put("Cache-Control", "max-age=0");

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/index.html", Map.of()))
                .version("HTTP/1.1")
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("쿼리가 있는 경우 InputStream to HttpRequest 파싱 로직 테스트")
    void parseHttpRequestWithQuery() throws Exception {
        //given
        String input = "GET /user/create?key1=value1&key2=value2 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n";

        Map<String, String> expectedHeader = new HashMap<>();
        expectedHeader.put("Host", "localhost:8080");
        expectedHeader.put("Connection", "keep-alive");
        expectedHeader.put("Cache-Control", "max-age=0");

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/user/create", Map.of("key1", "value1", "key2", "value2")))
                .version("HTTP/1.1")
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("바디가 있는 경우 InputStream to HttpRequest 파싱 로직 테스트")
    void parseHttpRequestWithBody() throws Exception {
        //given
        String input = "GET /user/create?key1=value1&key2=value2 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "Content-Type: 4\n" +
                "\n" +
                "test=test";

        Map<String, String> expectedHeader = new HashMap<>();
        expectedHeader.put("Host", "localhost:8080");
        expectedHeader.put("Connection", "keep-alive");
        expectedHeader.put("Cache-Control", "max-age=0");
        expectedHeader.put("Content-Length", "9");
        expectedHeader.put("Content-Type", "application/x-www-form-urlencoded");

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/user/create", Map.of("key1", "value1", "key2", "value2")))
                .version("HTTP/1.1")
                .body(Map.of("test", "test"))
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertEquals(expected, actual);
    }
}