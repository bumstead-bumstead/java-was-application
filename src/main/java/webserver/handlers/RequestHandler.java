package webserver.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.HttpMessageParser;
import webserver.http.message.HttpRequest;
import webserver.http.message.HttpResponse;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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
            HttpResponse httpResponse = HttpRequestRouter.getInstance().route(httpRequest);
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
