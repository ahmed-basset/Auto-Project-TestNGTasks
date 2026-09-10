package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
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

    By DatePostedFilter = By.xpath("//*[normalize-space(text())='Date Posted']");

    // The option's own text carries a job count that changes daily, and the radio it drives is
    // hidden behind a custom control, so the label is what gets clicked.
    By PastWeekOption = By.xpath("//label[.//span[starts-with(normalize-space(.), 'Past week')]]");

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

    public void OpenDatePostedFilter()
    {
        ClickElement(DatePostedFilter);
    }

    public void ChoosePastWeek()
    {
        ClickElement(PastWeekOption);
        wait.until(ExpectedConditions.urlContains("within_1_week"));
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(JobTitles));
    }

    public String GetFirstJobTitle()
    {
        return driver.findElements(JobTitles).get(0).getText().trim();
    }

    public void OpenFirstJobInNewTab()
    {
        String jobUrl = driver.findElements(JobTitles).get(0).getAttribute("href");
        // Ctrl+click depends on where the focus happens to be, so the tab is opened outright and the
        // job loaded into it.
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(jobUrl);
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
