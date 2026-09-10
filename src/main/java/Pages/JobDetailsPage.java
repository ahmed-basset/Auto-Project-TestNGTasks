package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class JobDetailsPage extends BasePage {

    public JobDetailsPage(WebDriver driver) {
        super(driver);
    }

    By ApplyForJobButton = By.xpath("//button[contains(normalize-space(.), 'Apply for Job')]");
    By SaveAndApplyLaterButton = By.xpath("//button[contains(normalize-space(.), 'Save and Apply')]");
    // The SAVED tab in the site header; its own class names are generated and change with each build.
    By SavedLink = By.cssSelector("#header-nav a[href='/saved']");

    public void PressApply()
    {
        ClickVisibleElement(ApplyForJobButton);
        // Applying opens a draft application form, and the form is where the later steps live.
        wait.until(ExpectedConditions.visibilityOfElementLocated(SaveAndApplyLaterButton));
    }

    public void SaveAndApplyLater()
    {
        ClickVisibleElement(SaveAndApplyLaterButton);
    }

    public void OpenSavedJobs()
    {
        ClickVisibleElement(SavedLink);
        wait.until(ExpectedConditions.urlContains("/saved"));
    }
}
