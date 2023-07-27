package application.RequestProcessor;

import application.HTMLRenderer.HTMLRendererManager;
import application.db.UserDatabase;
import application.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.BodyParameter;
import webserver.annotations.HandleRequest;
import webserver.exceptions.BadRequestException;
import webserver.http.message.*;
import webserver.http.session.Cookie;
import webserver.http.session.Session;
import webserver.http.session.SessionDatabase;

import java.util.HashMap;
import java.util.Map;

public class RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private HTMLRendererManager htmlRendererManager;

    private static class SingletonHelper {
        private static final RequestProcessor SERVICE_MANAGER = new RequestProcessor();
    }

    public static RequestProcessor getInstance() {
        return SingletonHelper.SERVICE_MANAGER;
    }

    private RequestProcessor() {
        this.htmlRendererManager = HTMLRendererManager.getInstance();
    }

    @HandleRequest(path = "/index.html", httpMethod = HttpMethod.GET)
    public HttpResponse index(Session session) throws Exception {
        byte[] body;
        Map<String, Object> parameters = new HashMap<>();

        if (session == null) {
            parameters.put("user", null);
        } else {
            String userId = (String) session.getAttribute("userId");
            User user = UserDatabase.findUserById(userId);
            parameters.put("user", user);
        }

        body = htmlRendererManager.render("/index.html", parameters);
        return new HttpResponse.Builder()
                .headers(HttpMessageHeader.generateDefaultHeader(body, MIME.HTML.contentType))
                .body(body)
                .build();
    }

    @HandleRequest(path = "/user/list.html", httpMethod = HttpMethod.GET)
    public HttpResponse userList(Session session) throws BadRequestException {
        Map<String, Object> parameters = new HashMap<>();

        if (session == null) {
            return HttpResponse.generateRedirect("http://localhost:8080/user/login.html");
        }

        String userId = (String) session.getAttribute("userId");
        User user = UserDatabase.findUserById(userId);
        parameters.put("user", user);

        byte[] body = htmlRendererManager.render("/user/list.html", parameters);
        return new HttpResponse.Builder()
                .headers(HttpMessageHeader.generateDefaultHeader(body, MIME.HTML.contentType))
                .body(body)
                .build();
    }

    @HandleRequest(path = "/user/login.html", httpMethod = HttpMethod.GET)
    public HttpResponse loginPage(Session session) throws BadRequestException {
        Map<String, Object> parameters = new HashMap<>();

        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            User user = UserDatabase.findUserById(userId);
            parameters.put("user", user);
        }

        byte[] body = htmlRendererManager.render("/user/login.html", parameters);
        return new HttpResponse.Builder()
                .headers(HttpMessageHeader.generateDefaultHeader(body, MIME.HTML.contentType))
                .body(body)
                .build();
    }

    @HandleRequest(path = "/user/form.html", httpMethod = HttpMethod.GET)
    public HttpResponse signUpPage(Session session) throws BadRequestException {
        Map<String, Object> parameters = new HashMap<>();

        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            User user = UserDatabase.findUserById(userId);
            parameters.put("user", user);
        }

        byte[] body = htmlRendererManager.render("/user/form.html", parameters);
        return new HttpResponse.Builder()
                .headers(HttpMessageHeader.generateDefaultHeader(body, MIME.HTML.contentType))
                .body(body)
                .build();
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
        session.addAttribute("userId", userId);
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
