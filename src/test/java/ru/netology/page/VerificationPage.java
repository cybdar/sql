package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    private void fillCodeAndSubmit(String code) {
        codeField.setValue(code);
        verifyButton.click();
    }

    public DashboardPage validVerify(String verificationCode) {
        fillCodeAndSubmit(verificationCode);
        return new DashboardPage();
    }

    public void invalidVerify(String verificationCode) {
        fillCodeAndSubmit(verificationCode);
        checkErrorNotificationText("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }

    public void checkErrorNotificationText(String expectedText) {
        errorNotification.shouldBe(visible, Duration.ofSeconds(10)).shouldHave(exactText(expectedText));
    }
}