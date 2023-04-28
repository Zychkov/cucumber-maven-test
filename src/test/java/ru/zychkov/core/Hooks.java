package ru.zychkov.core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.Selenide;
import ru.zychkov.core.custom.Config;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Slf4j
public class Hooks {

    @Before
    public void setupBrowser(Scenario scenario) {
        Collection<String> sourceTagNames = scenario.getSourceTagNames();
        sourceTagNames.forEach(s -> log.debug("TAG: " + s));

        Configuration.browser = Config.getInstance().getBrowser();
        Configuration.timeout = 20000;
        Configuration.pageLoadTimeout = 60000;
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = Config.getInstance().getPortalHost();

        if (Config.getInstance().getBrowser().equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--no-sandbox");

            Configuration.browserCapabilities = new DesiredCapabilities(options);
        }
    }

    @After
    public void logOut(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                File screenshot = Screenshots.takeScreenShotAsFile();
                assert (screenshot != null);
                InputStream targetStream = new FileInputStream(screenshot);
                Allure.addAttachment("Screenshot on fail", "image/png", targetStream, "png");
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }

        Selenide.closeWebDriver();
    }
}