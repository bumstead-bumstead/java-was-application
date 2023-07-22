package Application.Controller;

import Application.db.Database;
import Application.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.HandleRequest;
import webserver.annotations.QueryParameter;
import webserver.exceptions.BadRequestException;
import webserver.http.message.HttpMethod;
import webserver.http.message.HttpResponse;
import webserver.http.message.StatusCode;
import webserver.utils.HttpHeaderUtils;

import java.util.Map;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private static class SingletonHelper {
        private static final Controller CONTROLLER = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHelper.CONTROLLER;
    }

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.POST)
    public HttpResponse createUser(@QueryParameter(key = "userId") String userId,
                                   @QueryParameter(key = "password") String password,
                                   @QueryParameter(key = "name") String name,
                                   @QueryParameter(key = "email") String email) {
        try {
            User user = new User(userId, password, name, email);
            Database.addUser(user);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            return new HttpResponse.Builder()
                    .statusCode(StatusCode.BAD_REQUEST)
                    .build();
        }

        return new HttpResponse.Builder()
                .statusCode(StatusCode.FOUND)
                .headers(Map.of(HttpHeaderUtils.LOCATION_HEADER, "http://localhost:8080/index.html"))
                .build();
    }
}

