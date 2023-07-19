package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpMessageParser;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.*;
import java.net.Socket;

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
            HttpRequest httpRequest = HttpMessageParser.parseHttpRequest(in);

            printHttpRequestHeader(httpRequest);
            HttpResponse httpResponse = RequestRouter.route(httpRequest);
            response(out, httpResponse);
        } catch (Exception e) {
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
        logger.debug("method : " + httpRequest.getHttpMethod());
        logger.debug("version : " + httpRequest.getVersion());
        logger.debug("URI : " + httpRequest.getURI());
        logger.debug("headers : " + httpRequest.getHeaders());
    }
}
