package webserver.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.message.parser.HttpMessageParser;
import webserver.http.message.HttpRequest;
import webserver.http.message.HttpResponse;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private HttpRequestRouter httpRequestRouter;
    private HttpMessageParser httpMessageParser;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.httpRequestRouter = HttpRequestRouter.getInstance();
        this.httpMessageParser = HttpMessageParser.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = httpMessageParser.parseHttpRequest(in);
            printHttpRequestHeader(httpRequest);
            HttpResponse httpResponse = httpRequestRouter.route(httpRequest);
            response(out, httpResponse);
        } catch (Exception e) {
            logger.error(e.getMessage() + " : " + e.getClass().getName());
            e.printStackTrace();
        }
    }

    private void response(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        ByteArrayOutputStream outputStream = httpMessageParser.parseHttpResponse(httpResponse);

         dos.write(outputStream.toByteArray());
         dos.flush();
    }

    private synchronized static void printHttpRequestHeader(HttpRequest httpRequest) {
        logger.debug("============================request arrived=============================");
        logger.debug("method : " + httpRequest.getHttpMethod());
        logger.debug("version : " + httpRequest.getVersion());
        logger.debug("URI : " + httpRequest.getURI());
        logger.debug("headers : " + httpRequest.getHeaders());
        logger.debug("body : " + httpRequest.getBody());
        logger.debug("=======================================================================");
    }
}
