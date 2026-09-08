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

public class RegisterTest extends BaseTest {
    Faker faker = new Faker();
    RegisterPage registerPage;
    JobSearchPage jobSearchPage;

    private static final String SEARCHED_JOB_TITLE = "Software Engineer";

    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }

    String email;
    String pass;
    String firstname;
    String lastname;
    String mobileunumber;
    String dob;


    private void generateUser() {
        email = "qa_" + UUID.randomUUID() + "@mail.com";
        // The bare password() can come back without an uppercase letter, digit, or symbol, which the signup
        // form rejects; asking for all three keeps every generated account valid.
        pass = faker.internet().password(10, 14, true, true, true);
        firstname = faker.name().firstName();
        lastname = faker.name().lastName();
        mobileunumber = faker.regexify("01[0125][0-9]{8}");
        // The wizard rejects anyone under 16, so the generated birthday has to clear that floor.
        dob = new SimpleDateFormat("dd/MM/yyyy").format(faker.date().birthday(18, 50));
    }

    private void fillPersonalInformationPage() {
        generateUser();

        registerPage = new RegisterPage(DriverFactory.GetDriver());
        registerPage.BeginRegisteration();
        registerPage.EnterFN(firstname);
        registerPage.EnterLN(lastname);
        registerPage.ENterEM(email);
        registerPage.SetPass(pass);
        registerPage.CreateAccount();
        registerPage.Continue_1();

        registerPage.SetDateOfBirth(dob);
        registerPage.SetGender("female");
        registerPage.SetNationality("Egypt");
        registerPage.SetContry("Egypt");
        registerPage.SetCity("Alexandria");
        registerPage.SetArea("Montaza");
        registerPage.SetMobileNum(mobileunumber);
    }

    private void fillEducationPage() {
        registerPage.SetEducationLeVel("Bachelor's Degree");
        registerPage.SetFieldStudy("Accounting");
        registerPage.SetUniversity("Cairo University");
        registerPage.SetYearOfGraduation("2018");
    }

    private void fillExperiencePage() {
        registerPage.SetYearsOfExperience("No experience");
        registerPage.SetCareerLevel("Student");
    }

    // The expertise step refuses to submit without at least two skills and one language with a proficiency.
    private void fillExpertisePage() {
        registerPage.AddSkill("Software Testing");
        registerPage.AddSkill("Java");
        registerPage.SetLanguage("English");
        registerPage.SetLanguageProficiency("Fluent");
    }

    // The career interests step needs a job title, at least one job category, and a minimum salary
    // before "Get Started" will leave the page.
    private void fillCareerInterestsPage() {
        // The page arrives after a redirect, so wait for its heading before touching the fields.
        registerPage.CheckuserOnHisCareerInterestsPage();
        registerPage.SetJobTitle("Software Engineer");
        registerPage.SetJobCategory("IT");
        registerPage.SetMinimumSalary("5000");
    }

    @Test
    public void EnsureUserCanFillPersonalInformation() {
        fillPersonalInformationPage();
        SoftAssert softAssert = new SoftAssert();
        String personalInfoPageText = registerPage.CheckuserOnHisInformationPage();
        softAssert.assertTrue(personalInfoPageText.toLowerCase().contains("tell us about yourself"),
                "Expected the personal information page title to be shown. Actual text: " + personalInfoPageText);
        softAssert.assertAll();
    }

    @Test
    public void EnsureUserCanReachEducationPage() {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        SoftAssert softAssert = new SoftAssert();
        String educationPageText = registerPage.CheckuserOnHisEducationPage();
        softAssert.assertTrue(educationPageText.toLowerCase().contains("tell us about your education"),
                "Expected the education page title to be shown. Actual text: " + educationPageText);
        softAssert.assertAll();
    }
    @Test
    public void EnsureUserCanReachExperiencePage()
    {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        fillEducationPage();
        registerPage.Continue_3();
        SoftAssert softAssert = new SoftAssert();
        String experiencePageText = registerPage.CheckuserOnHisExperiencePage();
        softAssert.assertTrue(experiencePageText.toLowerCase().contains("tell us about your experience"),
                "Expected the experience page title to be shown. Actual text: " + experiencePageText);
        softAssert.assertAll();
    }

    @Test
    public void EnsureUserCanReachExpertisePage()
    {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        fillEducationPage();
        registerPage.Continue_3();
        fillExperiencePage();
        registerPage.Continue_4();
        SoftAssert softAssert = new SoftAssert();
        String expertisePageText = registerPage.CheckuserOnHisExpertisePage();
        softAssert.assertTrue(expertisePageText.toLowerCase().contains("tell us about your expertise"),
                "Expected the expertise page title to be shown. Actual text: " + expertisePageText);
        softAssert.assertAll();
    }

    @Test
    public void EnsureUserCanReachCareerInterestsPage()
    {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        fillEducationPage();
        registerPage.Continue_3();
        fillExperiencePage();
        registerPage.Continue_4();
        fillExpertisePage();
        registerPage.SaveAndContinue();
        SoftAssert softAssert = new SoftAssert();
        String careerInterestsPageText = registerPage.CheckuserOnHisCareerInterestsPage();
        softAssert.assertTrue(careerInterestsPageText.toLowerCase().contains("tell us about your career interests"),
                "Expected the career interests page title to be shown. Actual text: " + careerInterestsPageText);
        softAssert.assertAll();
    }

    @Test
    public void EnsureUserCanSearchForJobsAfterRegistration()
    {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        fillEducationPage();
        registerPage.Continue_3();
        fillExperiencePage();
        registerPage.Continue_4();
        fillExpertisePage();
        registerPage.SaveAndContinue();
        fillCareerInterestsPage();
        registerPage.StartUsingTheSite();

        jobSearchPage = new JobSearchPage(DriverFactory.GetDriver());
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
