package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class SavedJobsPage extends BasePage {

    public SavedJobsPage(WebDriver driver) {
        super(driver);
    }

    // The saved list wraps each title in a link to the job rather than in a heading like the results.
    By SavedJobTitles = By.cssSelector("a[href*='/jobs/p/']");
    By DeleteDraftButton = By.xpath("//button[normalize-space(.)='Delete Draft']");
    // Deleting asks for confirmation in a dialog that the site appends to the end of the page, so its
    // copy of the button is the last one.
    By ConfirmDeleteDraft = By.xpath("(//button[normalize-space(.)='Delete Draft'])[last()]");

    public void OpenSavedJobs()
    {
        driver.get("https://wuzzuf.net/saved");
    }

    // An empty saved page is an answer, not a failure, so the titles are read without waiting on them.
    public List<String> GetSavedJobTitles()
    {
        List<String> titles = new ArrayList<>();
        for (WebElement job : driver.findElements(SavedJobTitles))
        {
            titles.add(job.getText().trim());
        }
        return titles;
    }

    public void DeleteSavedDraft()
    {
        ClickVisibleElement(DeleteDraftButton);
        ClickVisibleElement(ConfirmDeleteDraft);
    }
}
