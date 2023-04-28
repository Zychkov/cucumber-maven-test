package ru.zychkov.core.custom;

import lombok.Getter;

@Getter
public class Config {

    private static final Config CONFIG;

    static {
        CONFIG = new Config();
        PropertyLoader propertyLoader = new PropertyLoader();
        propertyLoader.load(String.format("config/test-properties-%s.yaml", System.getProperty("profileId")), CONFIG);
    }

    public static Config getInstance() {
        return CONFIG;
    }

    @Property("portal.host")
    private String portalHost;

    @Property("default.timeout")
    private int defaultTimeout;

    @Property("testdata.path")
    private String testDataBasePath;

    @Property("browser")
    private String browser;

    @Property("remote.webdriver.host")
    private String remoteWebdriverHost;

    @Property("remote.webdriver.browser")
    private String remoteWebdriverBrowser;

    @Property("portal.username")
    private String portalUsername;

    @Property("portal.password")
    private String portalPassword;

}
