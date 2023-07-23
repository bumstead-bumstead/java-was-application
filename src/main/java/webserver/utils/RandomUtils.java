package webserver.utils;

import java.security.SecureRandom;

public class RandomUtils {
    public static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();

        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int iteration = 0; iteration < length; iteration++) {
            int randomIndex = secureRandom.nextInt(alphanumeric.length());
            stringBuilder.append(alphanumeric.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
