package Controller;

import db.Database;
import exceptions.BadRequestException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.HandleRequest;
import webserver.http.message.*;

import java.util.Map;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private static class SingletonHelper {
        private static final Controller CONTROLLER = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHelper.CONTROLLER;
    }

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.GET)
    public HttpResponse createUser(HttpRequest httpRequest) {
        try {
            URI uri = httpRequest.getURI();
            User user = User.of(uri.getParameters());

            Database.addUser(user);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            return HttpResponse.generateHttpResponse(StatusCode.BAD_REQUEST);
        }

        return HttpResponse.generateHttpResponse(StatusCode.FOUND, Map.of("Location", "http://localhost:8080/index.html"));
    }
}

