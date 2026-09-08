package Tests;

import Pages.RegisterPage;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RegisterTest extends BaseTest {
    Faker faker = new Faker();
    RegisterPage registerPage;

    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }

    String email = "qa_" + UUID.randomUUID() + "@mail.com";
    String pass = faker.internet().password();
    String firstname = faker.name().firstName();
    String lastname = faker.name().lastName();
    String mobileunumber = faker.regexify("01[0125][0-9]{8}");

    Date Dateofbirth = faker.date().birthday(12, 50);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String dob = sdf.format(Dateofbirth);

    private void fillPersonalInformationPage() {
        registerPage = new RegisterPage(driver);
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

        String personalInfoPageText = registerPage.CheckuserOnHisInformationPage();
        Assert.assertTrue(personalInfoPageText.toLowerCase().contains("tell us about yourself"),
                "Expected the personal information page title to be shown. Actual text: " + personalInfoPageText);
    }

    @Test
    public void EnsureUserCanReachEducationPage() {
        fillPersonalInformationPage();
        registerPage.Continue_2();

        String educationPageText = registerPage.CheckuserOnHisEducationPage();
        Assert.assertTrue(educationPageText.toLowerCase().contains("tell us about your education"),
                "Expected the education page title to be shown. Actual text: " + educationPageText);
    }
}
