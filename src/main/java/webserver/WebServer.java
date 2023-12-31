package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.RequestHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int N_THREAD = 100;

    public void run(String[] args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            ExecutorService executorService = Executors.newFixedThreadPool(N_THREAD);
            Socket connection;

            while ((connection = listenSocket.accept()) != null) {
                executorService.submit(new RequestHandler(connection));
            }
        }
    }
}
