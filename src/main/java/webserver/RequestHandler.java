package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMessageParser;
import webserver.message.HttpRequest;
import webserver.message.HttpResponse;
import webserver.message.StatusCode;

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
            /*
            *
            * 1. inputStream HttpRequest로 파싱
            * 2. method, path 보고 처리할 메소드 매핑
            * 3. HttpResponse 객체 생성
            * 4. flush();
            *
            * todo
            *  1. request랑 controller 매핑해주는 클래스
            *  2. response to byte[]
            * */
            HttpRequest httpRequest = HttpMessageParser.parseHttpResponse(in);

            printHttpRequestHeader(httpRequest);


            //todo : Http Method별 처리 로직
            byte[] body = httpRequest.getBytesOfGetRequest();
            HttpResponse httpResponse = HttpResponse.generateHttpResponse(StatusCode.OK, body);

            response(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        ByteArrayOutputStream outputStream = HttpMessageParser.parseHttpResponse(httpResponse);
        try {
            dos.write(outputStream.toByteArray());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void printHttpRequestHeader(HttpRequest httpRequest) {
        logger.debug("method : " + httpRequest.getMethod());
        logger.debug("version : " + httpRequest.getVersion());
        logger.debug("URI : " + httpRequest.getURI());
        logger.debug("metadata : " + httpRequest.getHeaders());
    }

//    private HttpRequest convertToHttpRequestHeader(InputStream in) throws IOException {
//        InputStreamReader inputStreamReader = new InputStreamReader(in);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//        return HttpRequest.createHttpRequestHeaderWithBufferedReader(bufferedReader);
//    }
}
