package Controller;

import Application.Controller.Controller;
import Application.db.UserDatabase;
import Application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.exceptions.BadRequestException;
import webserver.http.message.HttpResponse;
import webserver.http.message.StatusCode;
import webserver.utils.HttpHeaderUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {
    @Nested
    @DisplayName("유저 생성 테스트")
    class CreateUser {
        Controller controller;
        String userId;
        String password;
        String name;
        String email;
        @BeforeEach
        void setup() {
            UserDatabase.clear();
            controller = Controller.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";
        }

        @Test
        @DisplayName("올바른 입력에 대해서 응답으로 302번 코드를 보낸다.")
        void createUserTest() {
            HttpResponse httpResponse = controller.createUser(userId, password, name, email);

            assertEquals(StatusCode.FOUND, httpResponse.getStatusCode());
            assertThat(httpResponse.getHeaders().containsKey("Location"));
        }

        @Test
        @DisplayName("데이터베이스에 유저 정보를 저장한다.")
        void uploadToDatabase() {
            controller.createUser(userId, password, name, email);

            User actualUser = UserDatabase.findUserById("userId");
            User expectedUser = new User(userId, password, name, email);

            assertEquals(expectedUser, actualUser);
        }
    }

    @Nested
    @DisplayName("유저 로그인 테스트")
    class login {
        Controller controller;
        String userId;
        String password;
        String name;
        String email;
        @BeforeEach
        void setup() {
            UserDatabase.clear();
            controller = Controller.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";

            controller.createUser(userId, password, name, email);
        }

        @Test
        @DisplayName("올바른 로그인에 대해서 302를 응답으로 보낸다.")
        void login() throws BadRequestException {
            HttpResponse httpResponse = controller.login(userId, password);
            assertEquals(StatusCode.FOUND, httpResponse.getStatusCode());
        }

        @Test
        @DisplayName("올바른 로그인일 경우 응답 헤더에 cookie를 포함시킨다.")
        void loginCookieTest() throws BadRequestException {
            HttpResponse httpResponse = controller.login(userId, password);

            assertThat(httpResponse.getHeaders().containsKey("Set-Cookie"));
            String cookieValue = httpResponse.getHeaders().get("Set-Cookie");
            assertThat(cookieValue.contains("path=/"));
        }
        @Test
        @DisplayName("존재하지 않는 id인 경우, /user/login_failed.html로 redirection")
        void loginWithWrongId() throws BadRequestException {
            userId = "modifiedId";
            HttpResponse httpResponse = controller.login(userId, password);

            String expectedRedirectionURI = "http://localhost:8080/user/login_failed.html";
            String actualRedirectionURI = httpResponse.getHeaders().get(HttpHeaderUtils.LOCATION_HEADER);

            assertEquals(expectedRedirectionURI, actualRedirectionURI);
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않는 경우, /user/login_failed.html로 redirection")
        void loginWithWrongPwd() throws BadRequestException {
            password = "wrongPassword";
            HttpResponse httpResponse = controller.login(userId, password);

            String expectedRedirectionURI = "http://localhost:8080/user/login_failed.html";
            String actualRedirectionURI = httpResponse.getHeaders().get(HttpHeaderUtils.LOCATION_HEADER);

            assertEquals(expectedRedirectionURI, actualRedirectionURI);
        }
    }
}