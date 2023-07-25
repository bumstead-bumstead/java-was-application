package db;

import application.db.UserDatabase;
import application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.BadRequestException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class DatabaseTest {

    private User user;

    @BeforeEach
    void setUp() throws BadRequestException {
        Map<String, String> userParameters = new HashMap<>();
        userParameters.put("userId", "john_doe");
        userParameters.put("password", "secret_password");
        userParameters.put("name", "John Doe");
        userParameters.put("email", "john.doe@example.com");

        user = User.of(userParameters);
    }

    @Test
    @DisplayName("이미 존재하는 유저일 경우 BadRequestException을 발생시킨다.")
    void addUser() throws BadRequestException {
        UserDatabase.addUser(user);

        assertThatThrownBy(()-> UserDatabase.addUser(user))
                .isInstanceOf(BadRequestException.class);
    }
}