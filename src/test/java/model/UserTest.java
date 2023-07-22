package model;

import Application.model.User;
import webserver.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("john_doe", user.getUserId());
        assertEquals("secret_password", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Map으로 유효한 User 객체가 생성된다.")
    void testUserOf() throws BadRequestException {
        User user = User.of(validParameters);
        assertEquals("john_doe", user.getUserId());
        assertEquals("secret_password", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }
}