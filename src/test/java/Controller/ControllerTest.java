package Controller;

import Application.Controller.Controller;
import Application.db.Database;
import Application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.message.HttpResponse;
import webserver.http.message.StatusCode;

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
            Database.clear();
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

            User actualUser = Database.findUserById("userId");
            User expectedUser = new User(userId, password, name, email);

            assertEquals(expectedUser, actualUser);
        }
    }
}