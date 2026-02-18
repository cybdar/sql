package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".card");
    private SelenideElement logoutButton = $("[data-test-id=logout]");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public void checkDashboardVisible() {
        heading.shouldBe(visible);
    }

    public int getCardsCount() {
        return cards.size();
    }

    public void logout() {
        logoutButton.click();
    }
}
