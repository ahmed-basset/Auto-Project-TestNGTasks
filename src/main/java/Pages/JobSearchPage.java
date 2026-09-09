package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class JobSearchPage extends BasePage {

    public JobSearchPage(WebDriver driver) {
        super(driver);
    }


    By SearchBar = By.name("q");

    By JobTitles = By.xpath("//h2//a");

    By ResultsCount = By.xpath("//*[contains(normalize-space(.), 'Jobs found') or contains(normalize-space(.), 'Job found')]"
            + "[not(.//*[contains(normalize-space(.), 'Jobs found') or contains(normalize-space(.), 'Job found')])]");

    // The registration wizard ends on a page with no search box, so the search starts by coming here.
    public void OpenJobsPage()
    {
        driver.get("https://wuzzuf.net/jobs/egypt");
    }

    public void SearchForJob(String jobTitle)
    {
        Typing(SearchBar, jobTitle);
        Enter();
        // Searching leaves the browse page for /search/jobs, so wait for that before reading results.
        wait.until(ExpectedConditions.urlContains("/search/jobs"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(JobTitles));
    }

    public List<String> GetJobTitles()
    {
        return GetTexts(JobTitles);
    }

    public String GetResultsCountText()
    {
        return GetText(ResultsCount);
    }
}
