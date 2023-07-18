package utils;

import org.junit.jupiter.api.Test;
import webserver.message.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.junit.jupiter.api.Assertions.*;

class HttpMessageParserTest {

    @Test
    void parseHttpResponse() throws IOException {
        //given
        HttpResponse httpResponse = new HttpResponse(StatusCode.OK, new byte[0]);

        //when
        OutputStream outputStream = HttpMessageParser.parseHttpResponse(httpResponse);
        String output = outputStream.toString();

        //then
        assertThat(output.contains("Content-Length: 0"));
        assertThat(output.contains("Content-Type: text/html;charset=utf-8"));
        assertThat(output.startsWith("HTTP/1.1 200 OK"));
    }

    @Test
    void parseHttpRequest() throws IOException {
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
        HttpRequest expected = new HttpRequest(HttpMethod.GET,
                new URI("/index.html", Map.of()),
                "HTTP/1.1",
                expectedHeader);

        //when
        HttpRequest actual = HttpMessageParser.parseHttpRequest(inputStream);

        //then
        assertTrue(actual.equals(expected));
    }
}