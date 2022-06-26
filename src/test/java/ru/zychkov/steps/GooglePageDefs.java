package ru.zychkov.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.zychkov.pages.GooglePage;

import static com.codeborne.selenide.Selenide.open;

public class GooglePageDefs {

    GooglePage page = new GooglePage();

    @Given("open url {string}")
    public void openUrl(String url) {
        open(url );
    }

    @When("set value {string}")
    public void setValue(String value) {
        page.search(value);
    }

    @Then("check link {string}")
    public void checkLinkEn(String link) {
        page.checkLink(link);
    }

    @Когда("перехожу по url {string}")
    public void перехожуПоUrl(String url) {
        open(url );
    }

    @И("в ввожу в строку поиска {string}")
    public void вВвожуВСтрокуПоиска(String value) {
        page.search(value);
    }

    @Тогда("вижу ссылку {string}")
    public void вижуСсылку(String link) {
        page.checkLink(link);
    }
}