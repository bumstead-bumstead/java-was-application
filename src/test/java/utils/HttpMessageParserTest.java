package utils;

import application.model.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.message.*;
import webserver.http.message.parser.HttpMessageParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpMessageParserTest {

    @Test
    @DisplayName("HttpResponse to OutputStream 파싱 로직 테스트")
    void parseHttpResponse() throws IOException {
        //given
        HttpResponse httpResponse = new HttpResponse.Builder().build();

        //when
        OutputStream outputStream = HttpMessageParser.parseHttpResponse(httpResponse);
        String output = outputStream.toString();

        //then
        assertThat(output).startsWith("HTTP/1.1 200 OK");
    }

    @Test
    @DisplayName("HttpResponse to OutputStream 파싱 로직 테스트 (쿠키가 존재할 때)")
    void parseHttpResponseWithCookie() throws IOException {
        //given
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addContentLength(0)
                .addCookie(new Cookie("sid", "test"))
                .build();

        HttpResponse httpResponse = new HttpResponse.Builder()
                .headers(httpMessageHeader)
                .build();

        //when
        OutputStream outputStream = HttpMessageParser.parseHttpResponse(httpResponse);
        String output = outputStream.toString();

        //then
        assertThat(output).contains("Content-Length: 0");
        assertThat(output).contains("Set-Cookie: sid=test");
        assertThat(output).startsWith("HTTP/1.1 200 OK");
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
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .build();
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/index.html", Map.of()))
                .version("HTTP/1.1")
                .headers(httpMessageHeader)
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("쿠키가 있는 경우 InputStream to HttpRequest 파싱 로직 테스트")
    void parseHttpRequestWithCookie() throws Exception {
        //given
        String input = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "Cookie: sid=f20b38b8-55a4-48f8-9f7d-78c149f170c8";

        Map<String, String> expectedHeader = new HashMap<>();
        expectedHeader.put("Host", "localhost:8080");
        expectedHeader.put("Connection", "keep-alive");
        expectedHeader.put("Cache-Control", "max-age=0");

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .addCookie(new Cookie("sid", "f20b38b8-55a4-48f8-9f7d-78c149f170c8"))
                .build();

        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/index.html", Map.of()))
                .version("HTTP/1.1")
                .headers(httpMessageHeader)
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        System.out.println("actual = " + actual);
        System.out.println("expected = " + expected);

        //then
        assertThat(actual).isEqualTo(expected);
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
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .build();
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/user/create", Map.of("key1", "value1", "key2", "value2")))
                .version("HTTP/1.1")
                .headers(httpMessageHeader)
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("바디가 있는 경우 InputStream to HttpRequest 파싱 로직 테스트")
    void parseHttpRequestWithBody() throws Exception {
        //given
        String input = "GET /user/create?key1=value1&key2=value2 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "Content-Length: 9\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\n" +
                "test=test";

        Map<String, String> expectedHeader = new HashMap<>();
        expectedHeader.put("Host", "localhost:8080");
        expectedHeader.put("Connection", "keep-alive");
        expectedHeader.put("Cache-Control", "max-age=0");
        expectedHeader.put("Content-Length", "9");
        expectedHeader.put("Content-Type", "application/x-www-form-urlencoded");

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addHeader("Host", "localhost:8080")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .addContentLength(9)
                .addContentType("application/x-www-form-urlencoded")
                .build();
        HttpRequest expected = new HttpRequest.Builder()
                .httpMethod(HttpMethod.GET)
                .URI(new URI("/user/create", Map.of("key1", "value1", "key2", "value2")))
                .version("HTTP/1.1")
                .body(Map.of("test", "test"))
                .headers(httpMessageHeader)
                .build();

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}