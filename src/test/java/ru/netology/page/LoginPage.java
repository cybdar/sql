package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    private void fillLoginForm(DataHelper.AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
    }

    private void clickLoginButton() {
        loginButton.click();
    }

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        fillLoginForm(authInfo);
        clickLoginButton();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo authInfo) {
        fillLoginForm(authInfo);
        clickLoginButton();
        errorNotification.shouldBe(visible);
    }

    public void checkErrorNotificationVisible() {
        errorNotification.shouldBe(visible);
    }

    public void checkErrorNotificationText(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(exactText(expectedText));
    }
}