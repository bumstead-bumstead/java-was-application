package model;

import exceptions.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    private Map<String, String> validParameters;

    @BeforeEach
    public void setUp() {
        validParameters = new HashMap<>();
        validParameters.put("userId", "john_doe");
        validParameters.put("password", "secret_password");
        validParameters.put("name", "John Doe");
        validParameters.put("email", "john.doe@example.com");
    }

    @Test
    @DisplayName("생성자로 유효한 User 객체가 생성된다.")
    public void testUserConstructor() {
        User user = new User("john_doe", "secret_password", "John Doe", "john.doe@example.com");
        assertEquals("john_doe", user.getUserId());
        assertEquals("secret_password", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Map으로 유효한 User 객체가 생성된다.")
    public void testUserOf() {
        User user = User.of(validParameters);
        assertEquals("john_doe", user.getUserId());
        assertEquals("secret_password", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    @DisplayName("parameter 중 하나라도 존재하지 않는 경우 User.of는 IllegalParameterException을 발생시킨다.")
    public void testUserOfWithMissingParameters() {
        Map<String, String> invalidParameters = new HashMap<>(validParameters);
        invalidParameters.remove("name");

        assertThrows(IllegalParameterException.class, () -> {
            User.of(invalidParameters);
        });
    }
}