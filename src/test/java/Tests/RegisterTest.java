package Tests;

import Pages.RegisterPage;
import Utils.DriverFactory;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class RegisterTest extends BaseTest {
    Faker faker = new Faker();
    RegisterPage registerPage;

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
        pass = faker.internet().password();
        firstname = faker.name().firstName();
        lastname = faker.name().lastName();
        mobileunumber = faker.regexify("01[0125][0-9]{8}");
        dob = new SimpleDateFormat("dd/MM/yyyy").format(faker.date().birthday(12, 50));
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

    @Test
    public void EnsureUserCanFillPersonalInformation() {
        fillPersonalInformationPage();
        SoftAssert softAssert = new SoftAssert();
        String personalInfoPageText = registerPage.CheckuserOnHisInformationPage();
        softAssert.assertTrue(personalInfoPageText.toLowerCase().contains("tell us about yourself"),
                "Expected the personal information page title to be shown. Actual text: " + personalInfoPageText);
    }

    @Test
    public void EnsureUserCanReachEducationPage() {
        fillPersonalInformationPage();
        registerPage.Continue_2();
        SoftAssert softAssert = new SoftAssert();
        String educationPageText = registerPage.CheckuserOnHisEducationPage();
        softAssert.assertTrue(educationPageText.toLowerCase().contains("tell us about your education"),
                "Expected the education page title to be shown. Actual text: " + educationPageText);
    }
}
