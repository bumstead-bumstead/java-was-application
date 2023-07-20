package Controller;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.message.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {

    @Nested
    @DisplayName("유저 생성 테스트")
    class CreateUser {
        HttpRequest httpRequest;
        Controller controller;

        @BeforeEach
        void setup() {
            controller = Controller.getInstance();

            httpRequest = new HttpRequest(HttpMethod.GET,
                    new URI("/use/create",
                            Map.of("userId", "userId", "password", "password", "name", "name", "email", "email")),
                            "HTTP/1.1",
                            Map.of());
        }

        @Test
        @DisplayName("올바른 입력에 대해서 응답으로 200번 코드를 보낸다.")
        void receive200() {
            HttpResponse httpResponse = controller.createUser(httpRequest);

            assertEquals(httpResponse.getStatusCode(), StatusCode.OK);
        }

        @Test
        @DisplayName("데이터베이스에 유저 정보를 저장한다.")
        void uploadToDatabase() {
            controller.createUser(httpRequest);

            User actualUser = Database.findUserById("userId");
            User expectedUser = new User("userId", "password", "name", "email");

            assertEquals(expectedUser, actualUser);
        }
    }
}