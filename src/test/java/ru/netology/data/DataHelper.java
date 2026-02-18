package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    private DataHelper() {}

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getValidPetyaAuthInfo() {
        return new AuthInfo("petya", "123qwerty");
    }

    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo(faker.name().username(), faker.internet().password());
    }

    public static String getInvalidPasswordForUser(String login) {
        return faker.internet().password();
    }

    public static String getRandomVerificationCode() {
        return faker.numerify("######");
    }

    public static String getEncryptedPassword() {
        return "$2a$10$5aHX1epj6SBsdhUqN5eLkOc4UYl6h6ZGDnKG7S.8JlhqQhqoJv0J2";
    }
}