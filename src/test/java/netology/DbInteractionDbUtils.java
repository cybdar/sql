package netology;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.*;
import ru.netology.data.SQLHelper;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DbInteractionDbUtils {

    @BeforeEach
    @SneakyThrows
    void setUp() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @Order(1)
    @DisplayName("Should test verification codes workflow with DBUtils")
    void shouldWorkWithVerificationCodes() {
        assertDoesNotThrow(() -> {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app",
                    "app",
                    "pass")) {

                QueryRunner runner = new QueryRunner();

                String userId = java.util.UUID.randomUUID().toString();
                runner.update(conn,
                        "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')",
                        userId, "testuser", "password123");

                String testCode = "123456";
                runner.update(conn,
                        "INSERT INTO auth_codes(id, user_id, code) VALUES (?, ?, ?)",
                        java.util.UUID.randomUUID().toString(), userId, testCode);

                String retrievedCode = SQLHelper.getVerificationCodeForUser("testuser");

                assertNotNull(retrievedCode);
                assertEquals(testCode, retrievedCode);
            }
        });
    }

    @Test
    @Order(2)
    @DisplayName("Should test user status management with DBUtils")
    void shouldTestUserStatusManagement() {
        assertDoesNotThrow(() -> {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app",
                    "app",
                    "pass")) {

                QueryRunner runner = new QueryRunner();

                String testLogin = "status_test_user";
                runner.update(conn,
                        "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')",
                        java.util.UUID.randomUUID().toString(), testLogin, "password123");

                assertEquals("active", SQLHelper.getUserStatus(testLogin));

                SQLHelper.blockUser(testLogin);
                assertEquals("blocked", SQLHelper.getUserStatus(testLogin));

                SQLHelper.unblockUser(testLogin);
                assertEquals("active", SQLHelper.getUserStatus(testLogin));
            }
        });
    }

    @Test
    @Order(3)
    @DisplayName("Should test database constraints with DBUtils")
    void shouldTestDatabaseConstraints() {
        assertDoesNotThrow(() -> {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app",
                    "app",
                    "pass")) {

                QueryRunner runner = new QueryRunner();

                String login = "unique_user";
                runner.update(conn,
                        "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')",
                        java.util.UUID.randomUUID().toString(), login, "password123");

                assertThrows(Exception.class, () -> {
                    runner.update(conn,
                            "INSERT INTO users(id, login, password, status) VALUES (?, ?, ?, 'active')",
                            java.util.UUID.randomUUID().toString(), login, "password456");
                });
            }
        });
    }
}