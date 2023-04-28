package ru.zychkov.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class GooglePage {

    private final SelenideElement INPUT_SEARCH = $(By.xpath("//textarea[@name='q']"));
    private final SelenideElement BUTTON_ACCEPR_COOKIES = $("#L2AGLb");

    public void search(String value) {
        INPUT_SEARCH.val(value).pressEnter();
    }

    public void checkLink(String link) {
        String xPath = "//a[@href='" + link + "']";

        $(By.xpath(xPath)).shouldBe(Condition.visible);
        sleep(1000);
    }

    public void acceptCookies() {
        BUTTON_ACCEPR_COOKIES.click();
    }
}
