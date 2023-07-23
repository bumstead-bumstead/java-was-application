package Application.db;

import webserver.exceptions.BadRequestException;
import Application.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) throws BadRequestException {
        User existingUser = users.putIfAbsent(user.getUserId(), user);
        verifyDuplicatedInput(existingUser);
    }

    public static void clear() {
        users.clear();
    }

    private static void verifyDuplicatedInput(User existingUser) throws BadRequestException {
        if (existingUser != null) {
            throw new BadRequestException("이미 존재하는 유저 정보");
        }
    }

    public static void verifyLoginForm(String userId, String password) throws BadRequestException {
        User targetUser = UserDatabase.findUserById(userId);
        if (targetUser == null) {
            throw new BadRequestException("존재하지 않는 ID입니다.");
        }
        if (!password.equals(targetUser.getPassword())) {
            throw new BadRequestException("비밀번호가 틀렸습니다.");
        }
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
