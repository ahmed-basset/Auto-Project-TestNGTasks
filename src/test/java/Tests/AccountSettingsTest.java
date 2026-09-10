package Tests;

import Pages.AccountSettingsPage;
import Pages.JobDetailsPage;
import Pages.JobSearchPage;
import Pages.RegisterPage;
import Pages.SavedJobsPage;
import Utils.DriverFactory;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

// The whole journey on one account: it registers its own user and deletes it at the end, so it needs
// no fixed login and leaves nothing behind for the next run.
public class AccountSettingsTest extends BaseTest {

    Faker faker = new Faker();

    private static final String SEARCHED_JOB_TITLE = "Software Engineer";

    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }

    @Test
    public void EnsureUserCanRegisterSaveAJobAndDeleteTheAccount()
    {
        RegisterPage registerPage = new RegisterPage(DriverFactory.GetDriver());
        registerPage.CompleteRegistration(
                faker.name().firstName(),
                faker.name().lastName(),
                "qa_" + UUID.randomUUID() + "@mail.com",
                // The signup form wants an uppercase letter, a digit, and a symbol.
                faker.internet().password(10, 14, true, true, true),
                // The wizard rejects anyone under 16.
                new SimpleDateFormat("dd/MM/yyyy").format(faker.date().birthday(18, 50)),
                "female",
                "Egypt",
                "Egypt",
                "Alexandria",
                "Montaza",
                faker.regexify("01[0125][0-9]{8}"));

        JobSearchPage jobSearchPage = new JobSearchPage(DriverFactory.GetDriver());
        jobSearchPage.OpenJobsPage();
        jobSearchPage.SearchForJob(SEARCHED_JOB_TITLE);
        jobSearchPage.OpenDatePostedFilter();
        jobSearchPage.ChoosePastWeek();

        String jobToSave = jobSearchPage.GetFirstJobTitle();
        jobSearchPage.OpenFirstJobInNewTab();

        JobDetailsPage jobDetailsPage = new JobDetailsPage(DriverFactory.GetDriver());
        jobDetailsPage.PressApply();
        jobDetailsPage.SaveAndApplyLater();
        jobDetailsPage.OpenSavedJobs();

        SavedJobsPage savedJobsPage = new SavedJobsPage(DriverFactory.GetDriver());
        List<String> savedJobs = savedJobsPage.GetSavedJobTitles();
        Assert.assertTrue(savedJobs.contains(jobToSave),
                "Expected '" + jobToSave + "' to be listed on the saved jobs page. Actual list: " + savedJobs);

        AccountSettingsPage accountSettingsPage = new AccountSettingsPage(DriverFactory.GetDriver());
        accountSettingsPage.OpenProfileMenu();
        accountSettingsPage.OpenAccountSettings();
        accountSettingsPage.DeleteAccount("No longer looking for a job");

        String successMessage = accountSettingsPage.GetSuccessMessage();
        Assert.assertTrue(successMessage.toLowerCase().contains("deleted"),
                "Expected the site to confirm the account was deleted. Actual message: " + successMessage);
    }
}
