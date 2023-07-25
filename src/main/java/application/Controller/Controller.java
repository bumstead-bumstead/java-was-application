package application.Controller;

import application.db.SessionDatabase;
import application.db.UserDatabase;
import application.model.Cookie;
import application.model.Session;
import application.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.HandleRequest;
import webserver.annotations.BodyParameter;
import webserver.exceptions.BadRequestException;
import webserver.http.message.HttpMethod;
import webserver.http.message.HttpResponse;
import webserver.http.message.HttpMessageHeader;
import webserver.http.message.StatusCode;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private static class SingletonHelper {
        private static final Controller CONTROLLER = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHelper.CONTROLLER;
    }

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.POST)
    public HttpResponse createUser(@BodyParameter(key = "userId") String userId,
                                   @BodyParameter(key = "password") String password,
                                   @BodyParameter(key = "name") String name,
                                   @BodyParameter(key = "email") String email) {
        try {
            User user = new User(userId, password, name, email);
            UserDatabase.addUser(user);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            return HttpResponse.generateError(StatusCode.BAD_REQUEST);
        }

        return HttpResponse.generateRedirect("http://localhost:8080/index.html");
    }

    @HandleRequest(path = "/user/login", httpMethod = HttpMethod.POST)
    public HttpResponse login(@BodyParameter(key = "userId") String userId,
                              @BodyParameter(key = "password") String password) throws BadRequestException {
        try {
            verifyLoginForm(userId, password);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());
            return HttpResponse.generateRedirect("http://localhost:8080/user/login_failed.html");
        }

        Session session = new Session();
        SessionDatabase.addSession(session);

        Cookie cookie = new Cookie("sid", session.getId());

        HttpMessageHeader httpMessageHeader = new HttpMessageHeader.Builder()
                .addLocation("http://localhost:8080/index.html")
                .addCookie(cookie)
                .build();

        return new HttpResponse.Builder()
                .statusCode(StatusCode.FOUND)
                .headers(httpMessageHeader)
                .build();
    }

    private static void verifyLoginForm(String userId, String password) throws BadRequestException {
        User targetUser = UserDatabase.findUserById(userId);
        if (targetUser == null) {
            throw new BadRequestException("존재하지 않는 ID입니다.");
        }
        if (!password.equals(targetUser.getPassword())) {
            throw new BadRequestException("비밀번호가 틀렸습니다.");
        }
    }
}
