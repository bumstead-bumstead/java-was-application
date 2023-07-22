package db;

import com.google.common.collect.Maps;

import exceptions.BadRequestException;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) throws BadRequestException {
        if (users.containsKey(user.getUserId())) {
            throw new BadRequestException("이미 존재하는 유저 정보");
        }
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
