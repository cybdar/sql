package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {}

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/app",
                "app",
                "pass"
        );
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConnection()) {
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        }
    }

    @SneakyThrows
    public static String getUserId(String login) {
        var sql = "SELECT id FROM users WHERE login = ?";
        try (var conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        }
    }

    @SneakyThrows
    public static String getVerificationCode(String userId) {
        var sql = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), userId);
        }
    }

    @SneakyThrows
    public static String getVerificationCodeForUser(String login) {
        String userId = getUserId(login);
        if (userId == null) return null;
        return getVerificationCode(userId);
    }

    @SneakyThrows
    public static void addAuthCode(String userId, String code) {
        var sql = "INSERT INTO auth_codes(id, user_id, code) VALUES (?, ?, ?)";
        try (var conn = getConnection()) {
            runner.update(conn, sql, UUID.randomUUID().toString(), userId, code);
        }
    }

    @SneakyThrows
    public static String getUserStatus(String login) {
        var sql = "SELECT status FROM users WHERE login = ?";
        try (var conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        }
    }

    @SneakyThrows
    public static void setUserStatus(String login, String status) {
        var sql = "UPDATE users SET status = ? WHERE login = ?";
        try (var conn = getConnection()) {
            runner.update(conn, sql, status, login);
        }
    }

    @SneakyThrows
    public static void blockUser(String login) {
        setUserStatus(login, "blocked");
    }

    @SneakyThrows
    public static void unblockUser(String login) {
        setUserStatus(login, "active");
    }

    @SneakyThrows
    public static void addUser(String login, String passwordHash, String status) {
        var sql = "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, ?)";
        try (var conn = getConnection()) {
            runner.update(conn, sql, UUID.randomUUID().toString(), login, passwordHash, status);
        }
    }
}