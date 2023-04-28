package ru.zychkov.core;

import com.codeborne.selenide.WebDriverProvider;
import ru.zychkov.core.custom.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RemoteWebDriverProvider implements WebDriverProvider {

    protected boolean renderImages = Boolean.parseBoolean(System.getProperty("renderImages", "false"));
    private static final String EXTENSION_PATH = "src/test/resources/lib/chrome_extension/extension.zip";

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("profile.managed_default_content_settings.images", 2);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("ignore-certificate-errors");

        options.addExtensions(new File(EXTENSION_PATH));

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setAcceptInsecureCerts(true);

        options.setExperimentalOption("prefs", preferences);

        RemoteWebDriver driver = null;

        try {
            driver = new RemoteWebDriver(URI.create(Config.getInstance().getRemoteWebdriverHost()).toURL(), options);
            driver.setFileDetector(new LocalFileDetector());
        } catch (MalformedURLException exception) {
            log.error(exception.getLocalizedMessage());
        }

        assert (driver != null);

        return driver;
    }
}