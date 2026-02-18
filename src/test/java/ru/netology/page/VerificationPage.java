package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    private void fillCode(String code) {
        codeField.setValue(code);
    }

    private void clickVerifyButton() {
        verifyButton.click();
    }

    public DashboardPage validVerify(String verificationCode) {
        fillCode(verificationCode);
        clickVerifyButton();
        return new DashboardPage();
    }

    public void invalidVerify(String verificationCode) {
        fillCode(verificationCode);
        clickVerifyButton();
        errorNotification.shouldBe(visible);
    }

    public void checkErrorNotificationVisible() {
        errorNotification.shouldBe(visible);
    }

    public void checkErrorNotificationText(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(exactText(expectedText));
    }
}