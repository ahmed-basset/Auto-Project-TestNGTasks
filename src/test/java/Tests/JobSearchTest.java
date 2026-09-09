package Tests;

import Pages.JobSearchPage;
import Pages.RegisterPage;
import Utils.DriverFactory;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class JobSearchTest extends BaseTest {

    Faker faker = new Faker();
    RegisterPage registerPage;
    JobSearchPage jobSearchPage;

    private static final String SEARCHED_JOB_TITLE = "Software Engineer";

    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }

    @Test
    public void EnsureUserCanSearchForJobsAfterRegistration()
    {
        registerPage = new RegisterPage(DriverFactory.GetDriver());
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

        jobSearchPage = new JobSearchPage(DriverFactory.GetDriver());
        jobSearchPage.OpenJobsPage();
        jobSearchPage.SearchForJob(SEARCHED_JOB_TITLE);

        SoftAssert softAssert = new SoftAssert();

        List<String> jobTitles = jobSearchPage.GetJobTitles();
        softAssert.assertFalse(jobTitles.isEmpty(),
                "Expected the results page to list at least one job for '" + SEARCHED_JOB_TITLE + "'.");

        for (String jobTitle : jobTitles)
        {
            String normalized = jobTitle.toLowerCase();
            softAssert.assertTrue(normalized.contains("software") || normalized.contains("engineer"),
                    "Expected the listing to be relevant to '" + SEARCHED_JOB_TITLE + "'. Actual title: " + jobTitle);
        }

        String resultsCountText = jobSearchPage.GetResultsCountText();
        softAssert.assertTrue(resultsCountText.matches(".*\\d.*"),
                "Expected the number of search results to be displayed. Actual text: " + resultsCountText);

        softAssert.assertAll();
    }
}
