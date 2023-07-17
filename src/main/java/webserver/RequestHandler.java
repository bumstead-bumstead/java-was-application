package webserver;

import java.io.*;
import java.net.Socket;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final String TEMPLATES_PATH = "/Users/yohwan/IdeaProjects/be-was/src/main/resources/templates";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestHeader httpRequestHeader = convertToHttpRequestHeader(in);

            DataOutputStream dos = new DataOutputStream(out);

            //todo : Http Method별 처리 로직 추가
            byte[] body = getBytesOfGetRequest(httpRequestHeader);

            response200Header(dos, body.length);
//            response400Header(dos);
            responseBody(dos, body);
        } catch (FileNotFoundException e) {
            //todo : 404 에러 던지기
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] getBytesOfGetRequest(HttpRequestHeader httpRequestHeader) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(TEMPLATES_PATH + httpRequestHeader.getURI());

        return ByteStreams.toByteArray(fileInputStream);
    }

    private HttpRequestHeader convertToHttpRequestHeader(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        return HttpRequestHeader.createHttpRequestHeaderWithBufferedReader(bufferedReader);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400Header(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 400 Bad Reqeust");
        dos.flush();
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
