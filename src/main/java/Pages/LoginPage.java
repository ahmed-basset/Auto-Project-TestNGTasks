package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private String typedEmail = "";
    private String typedPassword = "";

    By Email = By.name("email");
    By Password = By.name("password");
    // "Sign in with Google" contains "Sign in" as well, so the text has to match exactly.
    By SignInButton = By.xpath("//button[normalize-space(.)='Sign in']");

    public void OpenLoginPage()
    {
        driver.get("https://wuzzuf.net/login");
    }

    public void SetEmail(String email)
    {
        typedEmail = email;
        Typing(Email, email);
    }

    public void SetPass(String password)
    {
        typedPassword = password;
        Typing(Password, password);
    }

    public void PressLogin()
    {
        // A click that lands before React takes the form over submits it natively and only adds a
        // "#" to the URL, so a sign-in that went nowhere is entered again.
        for (int attempt = 1; attempt <= 3; attempt++)
        {
            ClickElement(SignInButton);
            try
            {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
                return;
            }
            catch (TimeoutException e)
            {
                driver.get("https://wuzzuf.net/login");
                Typing(Email, typedEmail);
                Typing(Password, typedPassword);
            }
        }
        throw new TimeoutException("Signing in did not leave the login page.");
    }
}
