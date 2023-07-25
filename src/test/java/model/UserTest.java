package model;

import application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    private Map<String, String> validParameters;

    @BeforeEach
    void setUp() {
        validParameters = new HashMap<>();
        validParameters.put("userId", "john_doe");
        validParameters.put("password", "secret_password");
        validParameters.put("name", "John Doe");
        validParameters.put("email", "john.doe@example.com");
    }

    @Test
    @DisplayName("생성자로 유효한 User 객체가 생성된다.")
    void testUserConstructor() {
        User user = new User("john_doe", "secret_password", "John Doe", "john.doe@example.com");
        assertThat(user.getUserId()).isEqualTo("john_doe");
        assertThat(user.getPassword()).isEqualTo("secret_password");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Map으로 유효한 User 객체가 생성된다.")
    void testUserOf() throws BadRequestException {
        User user = User.of(validParameters);
        assertThat(user.getUserId()).isEqualTo("john_doe");
        assertThat(user.getPassword()).isEqualTo("secret_password");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }
}