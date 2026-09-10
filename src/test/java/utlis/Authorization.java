package utlis;

import Pages.LoginPage;
import Tests.BaseTest;
import Utils.DriverFactory;
import org.testng.annotations.BeforeMethod;

// Tests that need a signed-in user extend this instead of BaseTest.
public class Authorization extends BaseTest {

    // Registered once through the site's own signup wizard, so the job tests do not have to.
    private static final String EMAIL = "gp.automation.qa2026@mail.com";
    private static final String PASSWORD = "Qa!Automation2026";

    // BaseTest opens this before every test, so the session starts on the login page.
    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/login";
    }

    @BeforeMethod
    public void Login()
    {
        LoginPage loginPage = new LoginPage(DriverFactory.GetDriver());
        loginPage.SetEmail(EMAIL);
        loginPage.SetPass(PASSWORD);
        loginPage.PressLogin();
    }
}
