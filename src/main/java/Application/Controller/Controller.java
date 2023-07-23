package Application.Controller;

import Application.db.CookieDatabase;
import Application.db.UserDatabase;
import Application.model.Cookie;
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
            UserDatabase.addUser(user);
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

    @HandleRequest(path = "/user/login", httpMethod = HttpMethod.POST)
    public HttpResponse login(@QueryParameter(key = "userId") String userId,
                                   @QueryParameter(key = "password") String password) throws BadRequestException {
        try {
            UserDatabase.verifyLoginForm(userId, password);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            return new HttpResponse.Builder()
                    .statusCode(StatusCode.FOUND)
                    .headers(Map.of(HttpHeaderUtils.LOCATION_HEADER, "http://localhost:8080/user/login_failed.html"))
                    .build();
        }

        String cookieValue = HttpHeaderUtils.generateCookieHeader();
        Map<String, String> headers = Map.of(HttpHeaderUtils.LOCATION_HEADER,
                "http://localhost:8080/index.html",
                HttpHeaderUtils.SET_COOKIE_HEADER,
                cookieValue);

        Cookie cookie = new Cookie(cookieValue, userId);
        CookieDatabase.addCookie(cookie);

        return new HttpResponse.Builder()
                .statusCode(StatusCode.FOUND)
                .headers(headers)
                .build();
    }

}

