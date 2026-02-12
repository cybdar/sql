package netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public void validVerify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
    }

    public void invalidVerify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
        errorNotification.shouldBe(visible);
    }

    public void shouldShowErrorNotification() {
        errorNotification.shouldBe(visible);
    }
}