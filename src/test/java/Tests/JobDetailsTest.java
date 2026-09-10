package Tests;

import Pages.JobDetailsPage;
import Pages.JobSearchPage;
import Pages.SavedJobsPage;
import Utils.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utlis.Authorization;

import java.util.List;

// Saving a job needs a signed-in user, so this extends Authorization rather than BaseTest.
public class JobDetailsTest extends Authorization {

    JobSearchPage jobSearchPage;
    JobDetailsPage jobDetailsPage;
    SavedJobsPage savedJobsPage;

    private static final String SEARCHED_JOB_TITLE = "Software Engineer";

    @Test
    public void EnsureUserCanSaveAJobToApplyLater()
    {
        jobSearchPage = new JobSearchPage(DriverFactory.GetDriver());
        jobSearchPage.OpenJobsPage();
        jobSearchPage.SearchForJob(SEARCHED_JOB_TITLE);
        jobSearchPage.OpenDatePostedFilter();
        jobSearchPage.ChoosePastWeek();

        String jobToSave = jobSearchPage.GetFirstJobTitle();
        jobSearchPage.OpenFirstJobInNewTab();

        jobDetailsPage = new JobDetailsPage(DriverFactory.GetDriver());
        jobDetailsPage.PressApply();
        jobDetailsPage.SaveAndApplyLater();
        jobDetailsPage.OpenSavedJobs();

        savedJobsPage = new SavedJobsPage(DriverFactory.GetDriver());
        List<String> savedJobs = savedJobsPage.GetSavedJobTitles();
        Assert.assertTrue(savedJobs.contains(jobToSave),
                "Expected '" + jobToSave + "' to be listed on the saved jobs page. Actual list: " + savedJobs);
    }


    @AfterMethod
    public void DeleteTheDraftTheTestLeft()
    {
        SavedJobsPage savedJobs = new SavedJobsPage(DriverFactory.GetDriver());
        savedJobs.OpenSavedJobs();
        if (!savedJobs.GetSavedJobTitles().isEmpty())
        {
            savedJobs.DeleteSavedDraft();
        }
    }
}
