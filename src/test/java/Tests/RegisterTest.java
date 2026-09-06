package Tests;

import Pages.RegisterPage;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterTest extends BaseTest{
    Faker faker = new Faker();
    RegisterPage registerPage;
    @Override
    protected String getUrl() {
        return "https://wuzzuf.net/jobs/egypt";
    }


    String email = faker.internet().emailAddress();
    String pass = faker.internet().password();
    String name = faker.name().fullName();
    String firstname = faker.name().firstName();
    String lastname = faker.name().lastName();
    String address = faker.address().fullAddress();
    String state =  faker.address().state();
    String zipcode = faker.address().zipCode();
    String city = faker.address().city();
    // Egyptian mobile format: 01 + operator digit (0 Vodafone, 1 Etisalat, 2 Orange, 5 WE) + 8 digits
    String mobileunumber = faker.regexify("01[0125][0-9]{8}");

    Date Dateofbirth = faker.date().birthday(12,50);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String dob = sdf.format(Dateofbirth);



    @Test
    public  void EnsureUserCanCreateAccountWithValidCrendtails()
{
   registerPage =new RegisterPage(driver);
   registerPage.Register_1(firstname,lastname,email,pass,dob,"female","Egypt","Egypt","Alexandria","Montaza",mobileunumber);


}

}
