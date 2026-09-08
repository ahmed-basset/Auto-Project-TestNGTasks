package Tests;

import Utils.DriverFactory;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {


    private static final String[] BLOCKED_HOSTS = {
            "doubleclick.net", "*.doubleclick.net",
            "criteo.com", "*.criteo.com", "criteo.net", "*.criteo.net",
            "usersnap.com", "*.usersnap.com",
            "hotjar.com", "*.hotjar.com",
            "clarity.ms", "*.clarity.ms",
            "connect.facebook.net",
            "analytics.tiktok.com", "*.tiktokw.us",
            "www.googletagmanager.com",
            "www.google-analytics.com", "analytics.google.com",
            "*.sentry.io"
    };

    protected String getUrl() {
        return "";
    }

    @BeforeMethod
    public void Setup()
    {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        // 2 = block. Stops the browser permission bubbles that steal focus mid-test.
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_setting_values.geolocation", 2);

        options.setExperimentalOption("prefs", prefs);

        StringBuilder rules = new StringBuilder();
        for (String host : BLOCKED_HOSTS)
        {
            if (rules.length() > 0)
            {
                rules.append(",");
            }
            rules.append("MAP ").append(host).append(" 0.0.0.0");
        }
        options.addArguments("--host-resolver-rules=" + rules);

        options.addArguments("--disable-notifications");
        options.addArguments("--block-new-web-contents");
        options.addArguments("--disable-features=Translate");

        DriverFactory.SetDriver(new ChromeDriver(options));

        DriverFactory.GetDriver().manage().window().maximize();
        DriverFactory.GetDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        DriverFactory.GetDriver().get(getUrl());
    }

    @AfterMethod
    public void tearDown()
    {
        DriverFactory.QuitDriver();
    }
}
