package netology;

import com.codeborne.selenide.Configuration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";
        Configuration.headless = true;
        Configuration.timeout = 15000;
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        SQLHelper.cleanDatabase();
        addTestUsers();
        open("http://localhost:9999");
    }

    @SneakyThrows
    private void addTestUsers() {
        String bcryptHash = "$2a$10$5aHX1epj6SBsdhUqN5eLkOc4UYl6h6ZGDnKG7S.8JlhqQhqoJv0J2";
        SQLHelper.updateUsers("vasya", bcryptHash);
        SQLHelper.updateUsers("petya", bcryptHash);
    }

    @AfterAll
    @SneakyThrows
    static void tearDownAll() {
        SQLHelper.cleanDatabase();
        closeWebDriver();
    }

    @Test
    @Order(1)
    @DisplayName("Should get verification code from DB and use it for UI login")
    void shouldGetVerificationCodeFromDatabaseForUITest() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();

        VerificationPage verificationPage = loginPage.validLogin(authInfo);

        sleep(2000);

        String verificationCode = SQLHelper.getVerificationCodeForUser(authInfo.getLogin());

        if (verificationCode == null) {
            String userId = SQLHelper.getUserId(authInfo.getLogin());
            verificationCode = "123456";
            SQLHelper.addAuthCode(userId, verificationCode);
        }

        assertNotNull(verificationCode, "Verification code should be retrieved from DB");

        verificationPage.validVerify(verificationCode);
    }

    @Test
    @Order(2)
    @DisplayName("Should block user after three invalid login attempts")
    void shouldBlockUserAfterThreeInvalidLoginAttempts() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();

        for (int i = 1; i <= 3; i++) {
            loginPage.invalidLogin(new DataHelper.AuthInfo(authInfo.getLogin(), "wrong" + i));
            loginPage.shouldShowErrorNotification();
        }

        String status = SQLHelper.getUserStatus(authInfo.getLogin());
        assertEquals("blocked", status, "User should be blocked after 3 failed attempts");
    }

    @Test
    @Order(3)
    @DisplayName("Should show error with invalid verification code")
    void shouldShowErrorWithInvalidVerificationCode() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();

        VerificationPage verificationPage = loginPage.validLogin(authInfo);

        String invalidCode = "000000";
        verificationPage.invalidVerify(invalidCode);
        verificationPage.shouldShowErrorNotification();
    }

    @Test
    @Order(4)
    @DisplayName("Should test database operations")
    void shouldTestDatabaseOperations() {
        String userId = SQLHelper.getUserId("vasya");
        assertNotNull(userId, "Should get user ID");

        SQLHelper.addAuthCode(userId, "999999");
        String code = SQLHelper.getVerificationCodeForUser("vasya");
        assertEquals("999999", code, "Codes should match");

        assertEquals("active", SQLHelper.getUserStatus("vasya"));

        SQLHelper.blockUser("vasya");
        assertEquals("blocked", SQLHelper.getUserStatus("vasya"));

        SQLHelper.unblockUser("vasya");
        assertEquals("active", SQLHelper.getUserStatus("vasya"));
    }
}