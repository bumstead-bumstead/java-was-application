package Application.Controller;

import Application.db.Database;
import webserver.exceptions.BadRequestException;
import Application.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.HandleRequest;
import webserver.annotations.QueryParameter;
import webserver.http.message.*;
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

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.GET)
    public HttpResponse createUser(@QueryParameter(key = "userId") String userId,
                                   @QueryParameter(key = "password")String password,
                                   @QueryParameter(key = "name") String name,
                                   @QueryParameter(key = "email") String email /*HttpRequest httpRequest*/) {
        try {
//            URI uri = httpRequest.getURI();
//            User user = User.of(uri.getParameters());
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

