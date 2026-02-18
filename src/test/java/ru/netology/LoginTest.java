package ru.netology;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest {

    @BeforeEach
    void setUp() {
        SQLHelper.cleanDatabase();
        SQLHelper.addUser("vasya", DataHelper.getEncryptedPassword(), "active");
        SQLHelper.addUser("petya", DataHelper.getEncryptedPassword(), "active");
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("Should successfully login with verification code from database")
    void shouldSuccessfullyLoginWithVerificationCodeFromDatabase() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getValidAuthInfo();

        VerificationPage verificationPage = loginPage.validLogin(authInfo);

        String userId = SQLHelper.getUserId(authInfo.getLogin());
        String verificationCode = SQLHelper.getVerificationCode(userId);

        assertNotNull(verificationCode, "Verification code should be generated in database");

        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkDashboardVisible();
    }

    @Test
    @DisplayName("Should block user after three invalid login attempts")
    void shouldBlockUserAfterThreeInvalidLoginAttempts() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getValidAuthInfo();

        for (int i = 1; i <= 3; i++) {
            DataHelper.AuthInfo invalidAuthInfo = new DataHelper.AuthInfo(
                    authInfo.getLogin(),
                    DataHelper.getInvalidPasswordForUser(authInfo.getLogin())
            );
            loginPage.invalidLogin(invalidAuthInfo);
            loginPage.checkErrorNotificationVisible();
        }

        String status = SQLHelper.getUserStatus(authInfo.getLogin());
        assertEquals("blocked", status, "User should be blocked after 3 failed attempts");
    }

    @Test
    @DisplayName("Should show error with invalid verification code")
    void shouldShowErrorWithInvalidVerificationCode() {
        LoginPage loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getValidAuthInfo();

        VerificationPage verificationPage = loginPage.validLogin(authInfo);

        String invalidCode = DataHelper.getRandomVerificationCode();
        verificationPage.invalidVerify(invalidCode);
        verificationPage.checkErrorNotificationVisible();
    }
}