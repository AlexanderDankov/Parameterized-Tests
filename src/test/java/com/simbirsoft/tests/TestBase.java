package com.simbirsoft.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.simbirsoft.config.CredentialConfig;
import com.simbirsoft.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.lang.String.format;

public class TestBase {

    public static CredentialConfig credentials = ConfigFactory.create(CredentialConfig.class);

    @BeforeAll
    static void setup() {

        Configuration.startMaximized = true;
        SelenideLogger.addListener("AllureListener", new AllureSelenide());

        String remoteUrl = "selenoid.autotests.cloud";
        Configuration.remote = format("https://%s:%s@%s/wd/hub/", credentials.login(), credentials.password(), remoteUrl);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    public void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
