package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class JobSearchPage extends BasePage {

    public JobSearchPage(WebDriver driver) {
        super(driver);
    }


    By SearchBar = By.xpath("//input[@type='text' and ("
            + "contains(translate(@placeholder,'JOBTIL','jobtil'),'job title') or "
            + "contains(translate(@placeholder,'KEYWORD','keyword'),'keyword') or "
            + "contains(translate(@name,'QSEARCH','qsearch'),'search') or @name='q')]");


    By JobTitles = By.xpath("//h2//a");


    By ResultsCount = By.xpath("//*[contains(normalize-space(.), 'Jobs found') or contains(normalize-space(.), 'Job found')]"
            + "[not(.//*[contains(normalize-space(.), 'Jobs found') or contains(normalize-space(.), 'Job found')])]");

    public void SearchForJob(String jobTitle)
    {
        Typing(SearchBar, jobTitle);
        Enter();
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
