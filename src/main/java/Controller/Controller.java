package Controller;

import db.Database;
import exceptions.IllegalParameterException;
import model.User;
import webserver.annotations.HandleRequest;
import webserver.http.message.*;

import java.util.Map;

public class Controller {
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
        } catch (IllegalParameterException e) {
            return HttpResponse.generateHttpResponse(StatusCode.BAD_REQUEST);
        }

        return HttpResponse.generateHttpResponse(StatusCode.FOUND, Map.of("Location", "http://localhost:8080/index.html"));
    }
}

